from datetime import datetime
import json
from ssl import OP_ALL
from typing import Counter
import requests
from utils.APIkeys import WEATHER_API_KEY
from translate import Translator


try:
    with open("data/settings.json", "r", encoding='utf-8') as f:
        config = json.load(f)
except FileNotFoundError:
    config = {"clear_output": False}

def _(text):
    if config["idioma"] == "pt":
        return text
    translator = Translator(from_lang="pt", to_lang=config["idioma"])
    translation = translator.translate(text)
    return translation

def weather():
    cidade = input(_("Digite o nome da cidade: "))
    URL = f'http://api.openweathermap.org/data/2.5/weather?q={cidade}&appid={WEATHER_API_KEY}&lang={config["idioma"]}'

    response = requests.get(URL)
    data = response.json()

    if response.status_code == 200:
        # Extrair as informações necessárias do JSON de resposta
        clima = data['weather'][0]['description']
        temperatura_kelvin = data['main']['temp']
        temperatura_max_kelvin = data['main']['temp_max']
        temperatura_min_kelvin = data['main']['temp_min']
        umidade = data['main']['humidity']
        vento = data['wind']['speed']
        nascer_do_sol = datetime.fromtimestamp(data['sys']['sunrise']).strftime('%H:%M:%S')
        por_do_sol = datetime.fromtimestamp(data['sys']['sunset']).strftime('%H:%M:%S')

        # Converter as temperaturas para Celsius
        temperatura_celsius = temperatura_kelvin - 273.15
        temperatura_max_celsius = temperatura_max_kelvin - 273.15
        temperatura_min_celsius = temperatura_min_kelvin - 273.15

        print(_(f'Condições atuais em {cidade}:'))
        print(_(f'Temperatura: {temperatura_celsius:.2f}°C'))
        print(_(f'Temperatura máxima: {temperatura_max_celsius:.2f}°C'))
        print(_(f'Temperatura mínima: {temperatura_min_celsius:.2f}°C'))
        print(_(f'Umidade: {umidade}%'))
        print(_(f'Clima: {clima}'))
        print(_(f'Vento: {vento} m/s'))
        print(_(f'Nascer do Sol: {nascer_do_sol}'))
        print(_(f'Pôr do Sol: {por_do_sol}'))
    else:
        print('Erro ao obter a previsão do tempo.')


def settings():
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
                    json.dump(config, open("data/settings.json", "w", encoding='utf8'), indent=4)
                elif opcao == "idioma":
                    idioma = input("Digite o idioma que deseja utilizar: ")
                    config[opcao] = idioma # type: ignore
                    print(
                        f"Configuração alterada com sucesso | Novo valor de {opcao}: ",
                        config[opcao],
                    )
                    json.dump(config, open("data/settings.json", "w", encoding='utf8'), indent=4)
            else:
                print(("Configuração inexistente"))
        else:
            print(("Opção inválida"))
            
def show_post():
    with open("data/posts.json", "r", encoding="utf-8") as f:
        posts = json.load(f)
    print("Posts: ")
    for id, post in posts.items():
        print(f"{id}) {post['titulo']} - {post['autor']}")
    post_id = input("Digite o id do post que deseja ver: ")
    try:
        post_id = int(post_id)
    except:
        print("Digite um número")
        return
    if post_id not in posts.keys():
        print("Post inexistente")
        return
    post = posts[post_id]
    print(f"Post: {post['titulo']}")
        
def create_post(user):
    with open("data/posts.json", "r", encoding="utf-8") as f:
        posts = json.load(f)
        id = len(posts) + 1
    titulo = input(_("Digite o título do post: "))
    conteudo = input(_("Digite o conteúdo do post (digite \\n para pular de linha): "))
    tags = input(_("Digite as tags (por exemplo: dica, experiência, prática agrícola) do post (separado por virgulas): "))
    tags = tags.split(",")
    tags = [tag.strip() for tag in tags]
    posts.update({id : {"titulo": titulo, "conteudo": conteudo, "tags": tags, "autor": user["nome"], "user": user["email"]}})
    with open("data/posts.json", "w", encoding="utf-8") as f:
        json.dump(posts, f, indent=4)