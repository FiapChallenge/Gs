from datetime import datetime
import json
import requests
from utils.APIkeys import WEATHER_API_KEY
from translate import Translator

# Carrega as configurações do arquivo settings.json
try:
    with open("data/settings.json", "r", encoding="utf-8") as f:
        config = json.load(f)
except FileNotFoundError:
    config = {"clear_output": False}


# Checa se o language code é válido
def is_language_supported(lang_code):
    lang = [
        "pt-br",
        "af",
        "sq",
        "am",
        "ar",
        "hy",
        "az",
        "eu",
        "be",
        "bn",
        "bs",
        "bg",
        "ca",
        "ceb",
        "ny",
        "zh-cn",
        "zh-tw",
        "co",
        "hr",
        "cs",
        "da",
        "nl",
        "en",
        "eo",
        "et",
        "tl",
        "fi",
        "fr",
        "fy",
        "gl",
        "ka",
        "de",
        "el",
        "gu",
        "ht",
        "ha",
        "haw",
        "iw",
        "he",
        "hi",
        "hmn",
        "hu",
        "is",
        "ig",
        "id",
        "ga",
        "it",
        "ja",
        "jw",
        "kn",
        "kk",
        "km",
        "ko",
        "ku",
        "ky",
        "lo",
        "la",
        "lv",
        "lt",
        "lb",
        "mk",
        "mg",
        "ms",
        "ml",
        "mt",
        "mi",
        "mr",
        "mn",
        "my",
        "ne",
        "no",
        "or",
        "ps",
        "fa",
        "pl",
        "pt",
        "pa",
        "ro",
        "ru",
        "sm",
        "gd",
        "sr",
        "st",
        "sn",
        "sd",
        "si",
        "sk",
        "sl",
        "so",
        "es",
        "su",
        "sw",
        "sv",
        "tg",
        "ta",
        "te",
        "th",
        "tr",
        "uk",
        "ur",
        "ug",
        "uz",
        "vi",
        "cy",
        "xh",
        "yi",
        "yo",
        "zu",
    ]
    if lang_code in lang:
        return True
    else:
        return False


# Função para traduzir o texto para o idioma escolhido
def trad(text):
    if config["idioma"] == "pt" or config["idioma"] == "pt-br":
        return text
    translator = Translator(from_lang="pt", to_lang=config["idioma"])
    translation = translator.translate(text)
    return translation


# Função para mostrar o clima
def weather():
    print(trad("Opção escolhida: Clima"))
    cidade = input(trad("Digite o nome da cidade: "))
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

        print(trad(f"Condições atuais em {cidade}:"))
        print(trad(f"Temperatura: {temperatura_celsius:.2f}°C"))
        print(trad(f"Temperatura máxima: {temperatura_max_celsius:.2f}°C"))
        print(trad(f"Temperatura mínima: {temperatura_min_celsius:.2f}°C"))
        print(trad(f"Umidade: {umidade}%"))
        print(trad(f"Clima: {clima}"))
        print(trad(f"Vento: {vento} m/s"))
        print(trad(f"Nascer do Sol: {nascer_do_sol}"))
        print(trad(f"Pôr do Sol: {por_do_sol}"))
    else:
        print("Erro ao obter a previsão do tempo.")


# Função para mexer nas configurações
def settings():
    print(trad("Opção escolhida: Configurações"))
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
                    idioma = input(
                        trad(
                            "Digite o idioma que deseja utilizar (ex: en -> english, fr -> french, pt -> português): "
                        )
                    )
                    # check if language is valid
                    if not is_language_supported(idioma):
                        print(trad("Idioma inválido"))
                        continue
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


# Função que mostra os posts
def show_post():
    print(trad("Opção escolhida: Ver Posts"))
    with open("data/posts.json", "r", encoding="utf-8") as f:
        posts = json.load(f)
    print(trad("Posts: "))
    i = 1
    views = {}
    for post in posts.values():
        views.update({i: post})
        print(f"{i}) {post['titulo']} - {post['autor']}")
        i += 1
    try:
        post_id = int(
            input(trad("Digite o id do post que deseja ver (0 para voltar ao menu): "))
        )
    except:
        print(trad("Digite um número"))
        return
    if post_id == 0:
        return
    if post_id not in views.keys():
        print(trad("Post inexistente"))
        return
    post = views[post_id]
    print()
    print(f"Título: {post['titulo']}\n")
    conteudo = post["conteudo"].replace("\\n", "\n")
    print(f"{conteudo}\n")
    print()
    print(f"Autor: {post['autor']}")
    print(f"Tags: {', '.join(post['tags'])}")


# Função para criar um post
def create_post(user):
    print(trad("Opção escolhida: Criar um Post"))
    with open("data/posts.json", "r", encoding="utf-8") as f:
        posts = json.load(f)
        id = len(posts) + 1
    titulo = input(trad("Digite o título do post (0 para retornar ao menu): "))
    titulo = titulo.strip()
    if titulo == "" or titulo == "0":
        return
    conteudo = input(
        trad("Digite o conteúdo do post (digite \\n para pular de linha): ")
    )
    conteudo = conteudo.strip()
    if conteudo == "" or conteudo == "0":
        return
    tags = input(
        trad(
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


# Função para remover um post
def remove_post(user):
    print(trad("Opção escolhida: Remover um Post"))
    with open("data/posts.json", "r", encoding="utf-8") as f:
        posts = json.load(f)
    print(trad("Meus Posts: "))
    my_posts = {}
    i = 1
    for id, post in posts.items():
        if post["user"] == user["email"]:
            my_posts.update({i: id})
            print(f"{i}) {post['titulo']}")
            i += 1
    if len(my_posts) == 0:
        print(trad("Você não possui nenhum post"))
        return
    try:
        post_id = int(
            input(
                trad("Digite o id do post que deseja remover (0 para voltar ao menu): ")
            )
        )
    except:
        print(trad("Digite um número"))
        return
    if post_id == "0":
        return
    if post_id not in my_posts.keys():
        print(trad("Post inexistente"))
        return
    posts.pop(my_posts[post_id])
    with open("data/posts.json", "w", encoding="utf-8") as f:
        json.dump(posts, f, indent=4)
    print(trad("Post removido com sucesso"))


# Função para mostrar as perguntas frequentes
def faq():
    print(trad("Opção escolhida: FAQ"))
    print(trad("Perguntas Frequentes: "))
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
        },
        4: {
            "q": "Por que os posts não traduzem?",
            "a": "Por a tradução ainda estar em fase de testes, os posts não são traduzidos, apenas o menu e as mensagens do programa, para que não haja problemas de tradução de contexto",
        },
    }
    for id, question in questions.items():
        print(trad(f"{id}) {question['q']}"))
    question_id = input(
        trad("\nDigite o id da pergunta que deseja ver (0 para voltar ao menu): ")
    )
    if question_id == "0" or question_id == "":
        return
    if int(question_id) not in questions.keys():
        print(trad("Pergunta inexistente"))
        return
    question = questions[int(question_id)]
    print()
    print(trad(f"Pergunta: {question['q']}"))
    print(trad(f"{question['a']}"))


# Função para enviar sugestões de melhoria
def suggestions():
    print(trad("Opção escolhida: Sugestões de Melhoria"))
    with open("data/suggestions.json", "r", encoding="utf-8") as f:
        suggestions = json.load(f)
    sugestao = input(trad("Digite sua sugestão (0 para voltar ao menu): "))
    if sugestao == "0" or sugestao == "":
        return
    suggestions.update({len(suggestions) + 1: sugestao})
    with open("data/suggestions.json", "w", encoding="utf-8") as f:
        json.dump(suggestions, f, indent=4, ensure_ascii=False)
    print(trad("Sugestão enviada com sucesso"))
    print(trad("Obrigado por contribuir com o nosso projeto!"))


# Classe Lavoura com os métodos para adicionar, atualizar, remover e mostrar registros
class Lavoura:
    def __init__(self, user, nome, diario):
        self.user = user
        self.nome = nome
        self.diario = diario

    @staticmethod
    def verify_date_format(date):
        try:
            datetime.strptime(date, "%d/%m/%Y")
        except ValueError:
            return False
        return True

    def add_registro(self):
        data = input(
            trad("Digite a data do registro (dd/mm/aaaa) ('h' para o dia atual): ")
        )
        if data == "h":
            data = datetime.now().strftime("%d/%m/%Y")
        if not self.verify_date_format(data):
            print(trad("Data inválida"))
            return
        if data in [registro["Data"] for registro in self.diario]:
            sobrewrite_option = input(
                trad(
                    "Data já existente no diário, gostaria de sobrescrever o registro? (s/n): "
                )
            )
            if sobrewrite_option == "n":
                return
            if sobrewrite_option != "s":
                print(trad("Opção inválida"))
                return
            if sobrewrite_option == "s":
                self.remover_registro(data)
        registro = {
            "Data": data,
            "Clima": input(trad("Digite o clima do dia: ")),
            "Atividade Diária": input(trad("Digite a atividade diária: ")),
            "Estado da Lavoura": input(trad("Digite o estado da lavoura: ")),
            "Custos": input(trad("Digite o custo total do dia: ")),
            "Vendas": input(trad("Digite o ganho total do dia: ")),
            "Observações": input(trad("Digite as observações do dia: ")),
        }
        self.diario.append(registro)
        print("Registro adicionado com sucesso!")
        self.salvar_diario()

    def atualizar_registro(self, data, campo, novo_valor):
        campo = campo.strip().title()
        for registro in self.diario:
            if registro["Data"] == data:
                if campo not in registro.keys():
                    print("Campo inválido")
                    return
                if campo == "Data":
                    print("Não é possível alterar a data de um registro")
                    return
                registro[campo] = novo_valor
                print("Registro atualizado com sucesso!")
                self.salvar_diario()
                return
        print("Data não encontrada no diário.")

    def remover_registro(self, data):
        for registro in self.diario:
            if registro["Data"] == data:
                self.diario.remove(registro)
                print("Registro removido com sucesso!")
                self.salvar_diario()
                return
        print("Data não encontrada no diário.")

    def mostrar_diario(self):
        for registro in self.diario:
            print(f"Data: {registro['Data']}")
            print(f"Clima: {registro['Clima']}")
            print(f"Atividade Diária: {registro['Atividade Diária']}")
            print(f"Estado da Lavoura: {registro['Estado da Lavoura']}")
            print(f"Custos: {registro['Custos']}")
            print(f"Vendas: {registro['Vendas']}")
            print(f"Observações: {registro['Observações']}")
            print()

    def salvar_diario(self):
        with open(f"data/lavouras.json", "r", encoding="utf-8") as f:
            lavouras = json.load(f)
            try:
                my_lavoura = lavouras[self.user["email"]]
            except:
                my_lavoura = {}
                lavouras.update({self.user["email"]: {}})
        with open(f"data/lavouras.json", "w", encoding="utf-8") as f:
            lavouras[self.user["email"]][self.nome] = self.diario
            json.dump(lavouras, f, indent=4)


# Função para mexer no diário agrícola
def diary(user):
    print(trad("Opção escolhida: Diário Agrícola"))
    with open(f"data/lavouras.json", "r", encoding="utf-8") as f:
        lavouras = json.load(f)
        try:
            my_lavouras = lavouras[user["email"]]
        except:
            my_lavouras = {}
            lavouras.update({user["email"]: {}})
    print(trad("Minhas Lavouras: "))
    my_lavouras_names = {}
    i = 1
    for lavoura in my_lavouras.keys():
        my_lavouras_names.update({i: lavoura})
        print(f"{i}) {lavoura}")
        i += 1
    if len(my_lavouras_names) == 0:
        print(trad("Você não possui nenhuma lavoura"))
    lavoura_id = input(
        trad(
            "\nDigite 'c' se deseja criar uma nova lavoura, ou digite o índice da lavoura que deseja visualizar: "
        )
    )
    if lavoura_id == "c":
        nome = input(trad("Digite o nome da lavoura: "))
        lavoura = Lavoura(user, nome, [])
        lavoura.salvar_diario()
        print(trad("Lavoura criada com sucesso"))
        return
    if lavoura_id == "0":
        return
    if int(lavoura_id) not in my_lavouras_names.keys():
        print(trad("Lavoura inexistente"))
        return
    lavoura = Lavoura(
        user,
        my_lavouras_names[int(lavoura_id)],
        my_lavouras[my_lavouras_names[int(lavoura_id)]],
    )
    while True:
        menu = f"""Lavoura Escolhida: {lavoura.nome}
1 - Adicionar Registro
2 - Atualizar Registro
3 - Remover Registro
4 - Mostrar Diário
5 - Voltar ao Menu
        """
        print(trad(menu))
        opcao = input(trad("Digite a opção desejada: "))
        if opcao == "1":
            lavoura.add_registro()
        elif opcao == "2":
            data = input(
                trad(
                    "Digite a data do registro que deseja atualizar (dd/mm/aaaa) (h para hoje): "
                )
            )
            if data == "h":
                data = datetime.now().strftime("%d/%m/%Y")
            if not lavoura.verify_date_format(data):
                print(trad("Data inválida"))
                continue
            campo = input(trad("Digite o campo que deseja atualizar: "))
            novo_valor = input(trad("Digite o novo valor: "))
            lavoura.atualizar_registro(data, campo, novo_valor)
        elif opcao == "3":
            data = input(
                trad(
                    "Digite a data do registro que deseja remover (dd/mm/aaaa) ('h' para hoje): "
                )
            )
            if data == "h":
                data = datetime.now().strftime("%d/%m/%Y")
            if not lavoura.verify_date_format(data):
                print(trad("Data inválida"))
                continue
            lavoura.remover_registro(data)
        elif opcao == "4":
            lavoura.mostrar_diario()
        elif opcao == "5":
            lavoura.salvar_diario()
            return
        else:
            print(trad("Opção inválida"))
        print()
