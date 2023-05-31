import json
import re
import os
from utils.commands import *
from utils.commands import _


users = json.load(open("data/users.json", "r"))

def cadastro():
    print(_("Bem vindo(a) ao AgroSolution"))
    opcao = input("Você já possui cadastro? (s/n): ")
    if opcao == "s":
        user = login()
        os.system("cls" if os.name == "nt" else "clear")
        return user
    elif opcao == "n":
        cadastrado = False
        while not cadastrado:
            nome = input("Digite seu nome: ").strip().capitalize()
            if len(nome) < 5:
                print(_("Nome muito curto"))
                continue
            email = input("Digite seu email: ")
            # check if email is valid
            if not re.match(r"[^@ \t\r\n]+@[^@ \t\r\n]+\.[^@ \t\r\n]+", email):
                print(_("Email inválido"))
                continue
            if email in users:
                print(_("Email já cadastrado"))
                continue
            senha = input("Digite sua senha: ")
            if " " in senha:
                print(_("Senha não pode conter espaços"))
                continue
            if len(senha) < 5:
                print(_("Senha muito curta"))
                continue
            users[email] = {"nome": nome, "email": email, "senha": senha}
            json.dump(users, open("users.json", "w"), indent=4)
            print(_("Cadastro realizado com sucesso"))
            cadastrado = True
            return users[email]
        else:
            print(_("Opção inválida"))
            cadastro()
    else:
        print(_("Opção inválida"))
        cadastro()


def login():
    logged = False
    while not logged:
        email = input("Digite seu email: ")
        senha = input("Digite sua senha: ")
        if email in users:
            if senha == users[email]["senha"]:
                print(_("Login realizado com sucesso"))
                return users[email]
            else:
                print(_("Senha incorreta"))
        else:
            print(_("Email não cadastrado"))


def save_data():
    json.dump(users, open("data/users.json", "w", encoding='utf8'), indent=4)
    json.dump(config, open("data/settings.json", "w", encoding='utf8'), indent=4)


def print_menu():
    menu = """
--------------------------
          Menu
0 - Sair
1 - Configurações
2 - Clima
3 - Ver Posts
4 - Criar um Post
--------------------------
    """
    print(_(menu))


if __name__ == "__main__":
    if config["debug"]:
        user = users["augustobb@live.com"]
    else:
        user = cadastro()
    if user is not None:
        print(_(f"Bem vindo(a) {user['nome']}"))
        while True:
            print_menu()
            opcao = input("Digite a opção desejada: ")
            print()
            if config["clear_output"]:
                os.system("cls" if os.name == "nt" else "clear")
            try:
                opcao = int(opcao)
            except:
                print(_("Digite um número"))
                continue
            match opcao:
                case 0:
                    save_data()
                    exit()
                case 1:
                    settings()
                case 2:
                    weather()
                case 3:
                    show_post()
                case 4:
                    create_post(user)
                case _:
                    print(_("Opção inexistente"))
