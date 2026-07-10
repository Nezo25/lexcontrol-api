

async function test() {
  try {
    const res = await fetch('http://localhost:8080/clientes', {
      method: 'POST',
      headers: {
        'Origin': 'http://localhost:3000',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        nomeCliente: "Joao",
        cpf: "123",
        rg: "123",
        telefone: "123",
        dataDeVencimento: "2026-09-20",
        causa: "TRABALHISTA",
        statusPagamento: "PENDENTE",
        modeloDePagamento: "A_VISTA",
        valorCausa: 40000,
        valorParcela: 0,
        totalHonorarios: 500
      })
    });
    console.log("Status:", res.status);
    console.log("Headers:", res.headers.raw());
    const text = await res.text();
    console.log("Body:", text);
  } catch(e) {
    console.error("Fetch Error:", e);
  }
}
test();
