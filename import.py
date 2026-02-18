import os
import subprocess
import sys

def run_command(command, cwd=None):
    try:
        print(f"-> Executando: {' '.join(command)}")
        subprocess.check_call(command, cwd=cwd)
    except subprocess.CalledProcessError as e:
        print(f"Erro ao executar o comando: {e}")
        return False
    return True

def setup_lexcontrol():
    # URL do Repositório Mestre
    repo_url = "https://github.com/Nezo25/lexcontrol-api.git"
    
    # Definição das pastas (Instâncias)
    base_path = os.getcwd()
    main_dir = os.path.join(base_path, "lexcontrol-main")
    back_dir = os.path.join(base_path, "lexcontrol-back")
    front_dir = os.path.join(base_path, "lexcontrol-front")

    print("--- PROTOCOLO DE REENCARNAÇÃO LEXCONTROL ---")

    # 1. Clonar a Alma (Master) na pasta Main
    if not os.path.exists(main_dir):
        run_command(["git", "clone", repo_url, main_dir])
    
    os.chdir(main_dir)

    # 2. Limpar registros fantasmas (Pruning)
    # Isso remove referências a pastas que não existem mais (como as do Downloads)
    print("\nLimpando registros de instâncias antigas...")
    run_command(["git", "worktree", "prune"])

    # 3. Instanciar o BackEnd
    if not os.path.exists(back_dir):
        print("\nCriando instância: BackEnd...")
        run_command(["git", "worktree", "add", back_dir, "BackEnd"])
    else:
        print(f"\nAviso: Pasta {back_dir} já existe.")

    # 4. Instanciar o FrontEnd
    if not os.path.exists(front_dir):
        print("\nCriando instância: FrontEnd...")
        run_command(["git", "worktree", "add", front_dir, "FrontEnd"])
    else:
        print(f"\nAviso: Pasta {front_dir} já existe.")

    print("\n--- ESTRUTURA SINCRONIZADA ---")
    print(f"Main (Master):  {main_dir}")
    print(f"Back (BackEnd): {back_dir}")
    print(f"Front (FrontEnd): {front_dir}")

if __name__ == "__main__":
    setup_lexcontrol()