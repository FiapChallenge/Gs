from datetime import datetime
import json
from ssl import OP_ALL
from typing import Counter
import requests
from utils.APIkeys import WEATHER_API_KEY
from translate import Translator


try:
    with open("data/settings.json", "r", encoding="utf-8") as f:
        config = json.load(f)
except FileNotFoundError:
    config = {"clear_output": False}


def _(text):
    if config["idioma"] == "pt" or config["idioma"] == "pt-br":
        return text
    translator = Translator(from_lang="pt", to_lang=config["idioma"])
    translation = translator.translate(text)
    return translation


def weather():
    print(_("Opção escolhida: Clima"))
    cidade = input(_("Digite o nome da cidade: "))
    URL = f'http://api.openweathermap.org/data/2.5/weather?q={cidade}&appid={WEATHER_API_KEY}&lang={config["idioma"]}'

    response = requests.get(URL)
    data = response.json()

    if response.status_code == 200:
        # Extrair as informações necessárias do JSON de resposta
        clima = data["weather"][0]["description"]
        temperatura_kelvin = data["main"]["temp"]
        temperatura_max_kelvin = data["main"]["temp_max"]
        temperatura_min_kelvin = data["main"]["temp_min"]
        umidade = data["main"]["humidity"]
        vento = data["wind"]["speed"]
        nascer_do_sol = datetime.fromtimestamp(data["sys"]["sunrise"]).strftime(
            "%H:%M:%S"
        )
        por_do_sol = datetime.fromtimestamp(data["sys"]["sunset"]).strftime("%H:%M:%S")

        # Converter as temperaturas para Celsius
        temperatura_celsius = temperatura_kelvin - 273.15
        temperatura_max_celsius = temperatura_max_kelvin - 273.15
        temperatura_min_celsius = temperatura_min_kelvin - 273.15

        print(_(f"Condições atuais em {cidade}:"))
        print(_(f"Temperatura: {temperatura_celsius:.2f}°C"))
        print(_(f"Temperatura máxima: {temperatura_max_celsius:.2f}°C"))
        print(_(f"Temperatura mínima: {temperatura_min_celsius:.2f}°C"))
        print(_(f"Umidade: {umidade}%"))
        print(_(f"Clima: {clima}"))
        print(_(f"Vento: {vento} m/s"))
        print(_(f"Nascer do Sol: {nascer_do_sol}"))
        print(_(f"Pôr do Sol: {por_do_sol}"))
    else:
        print("Erro ao obter a previsão do tempo.")


def settings():
    print(_("Opção escolhida: Configurações"))
    # TODO - Alterar Idioma da settings
    for key, value in config.items():
        print((f"{key}: {value}"))
    resposta = ""
    while resposta != "n":
        resposta = input(("\nDeseja alterar alguma configuração? (s/n): ")).lower()
        if resposta == "n":
            break
        elif resposta == "s":
            opcao = input(("Digite o nome da configuração que deseja modificar: "))
            if opcao in config.keys():
                if type(config[opcao]) == bool:
                    config[opcao] = not config[opcao]
                    print(
                        f"Configuração alterada com sucesso | Novo valor de {opcao}: ",
                        config[opcao],
                    )
                    json.dump(
                        config,
                        open("data/settings.json", "w", encoding="utf8"),
                        indent=4,
                    )
                elif opcao == "idioma":
                    idioma = input(_("Digite o idioma que deseja utilizar: "))
                    config[opcao] = idioma  # type: ignore
                    print(
                        f"Configuração alterada com sucesso | Novo valor de {opcao}: ",
                        config[opcao],
                    )
                    json.dump(
                        config,
                        open("data/settings.json", "w", encoding="utf8"),
                        indent=4,
                    )
            else:
                print(("Configuração inexistente"))
        else:
            print(("Opção inválida"))


def show_post():
    print(_("Opção escolhida: Ver Posts"))
    with open("data/posts.json", "r", encoding="utf-8") as f:
        posts = json.load(f)
    print(_("Posts: "))
    i = 1
    views = {}
    for post in posts.values():
        views.update({i: post})
        print(f"{i}) {post['titulo']} - {post['autor']}")
        i += 1
    try:
        post_id = int(
            input(_("Digite o id do post que deseja ver (0 para voltar ao menu): "))
        )
    except:
        print(_("Digite um número"))
        return
    if post_id == 0:
        return
    if post_id not in views.keys():
        print(_("Post inexistente"))
        return
    post = views[post_id]
    print()
    print(f"Título: {post['titulo']}\n")
    conteudo = post["conteudo"].replace("\\n", "\n")
    print(f"{conteudo}\n")
    print()
    print(f"Autor: {post['autor']}")
    print(f"Tags: {', '.join(post['tags'])}")


def create_post(user):
    print(_("Opção escolhida: Criar um Post"))
    with open("data/posts.json", "r", encoding="utf-8") as f:
        posts = json.load(f)
        id = len(posts) + 1
    titulo = input(_("Digite o título do post: "))
    conteudo = input(_("Digite o conteúdo do post (digite \\n para pular de linha): "))
    tags = input(
        _(
            "Digite as tags (por exemplo: dica, experiência, prática agrícola) do post (separado por virgulas): "
        )
    )
    tags = tags.split(",")
    tags = [tag.strip() for tag in tags]
    posts.update(
        {
            id: {
                "titulo": titulo,
                "conteudo": conteudo,
                "tags": tags,
                "autor": user["nome"],
                "user": user["email"],
            }
        }
    )
    with open("data/posts.json", "w", encoding="utf-8") as f:
        json.dump(posts, f, indent=4)


def faq():
    print(_("Opção escolhida: FAQ"))
    print(_("Perguntas Frequentes: "))
    questions = {
        1: {
            "q": "Por que o clima não aparece?",
            "a": "Siga esses passos:\n1 - Verifique se o nome da cidade está escrito corretamente\n2 - Verifique se está conectado a internet\n3 - Se o probelma persistir, talvez a API esteja fora do ar ou tenha atingido o número máximo, por favor tente novamente mais tarde",
        },
        2: {
            "q": "Por que a tradução não funciona?",
            "a": "Siga esses passos:\n1 - Verifique se o idioma está escrito corretamente\n2 - Verifique se está conectado a internet\n3 - Se o probelma persistir, talvez a API esteja fora do ar ou tenha atingido o número máximo, por favor tente novamente mais tarde",
        },
        3: {
            "q": "Por que a tradução não é perfeita?",
            "a": "A API de tradução não é perfeita, ela apenas traduz o texto, não o contexto, por isso, algumas traduções podem não fazer sentido",
        }
    }
    for id, question in questions.items():
        print(f"{id}) {question['q']}")
    question_id = input(
        _("\nDigite o id da pergunta que deseja ver (0 para voltar ao menu): ")
    )
    if question_id == "0":
        return
    if int(question_id) not in questions.keys():
        print(_("Pergunta inexistente"))
        return
    question = questions[int(question_id)]
    print()
    print(f"Pergunta: {question['q']}")
    print(f"{question['a']}")


def suggestions():
    print(_("Opção escolhida: Sugestões de Melhoria"))
    with open("data/suggestions.json", "r", encoding="utf-8") as f:
        suggestions = json.load(f)
    sugestao = input(_("Digite sua sugestão: "))
    suggestions.update({len(suggestions) + 1: sugestao})
    with open("data/suggestions.json", "w", encoding="utf-8") as f:
        json.dump(suggestions, f, indent=4, ensure_ascii=False)
    print(_("Sugestão enviada com sucesso"))
    print(_("Obrigado por contribuir com o nosso projeto!"))


def remove_post(user):
    print(_("Opção escolhida: Remover um Post"))
    with open("data/posts.json", "r", encoding="utf-8") as f:
        posts = json.load(f)
    print(_("Meus Posts: "))
    my_posts = {}
    i = 1
    for id, post in posts.items():
        if post["user"] == user["email"]:
            my_posts.update({i: id})
            print(f"{i}) {post['titulo']}")
        i += 1
    if len(my_posts) == 0:
        print(_("Você não possui nenhum post"))
        return
    try:
        post_id = int(
            input(_("Digite o id do post que deseja remover (0 para voltar ao menu): "))
        )
    except:
        print(_("Digite um número"))
        return
    if post_id == "0":
        return
    if post_id not in my_posts.keys():
        print(_("Post inexistente"))
        return
    posts.pop(my_posts[post_id])
    with open("data/posts.json", "w", encoding="utf-8") as f:
        json.dump(posts, f, indent=4)
    print(_("Post removido com sucesso"))
