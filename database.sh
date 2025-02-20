#!/bin/bash

COMPOSE_FILE="docker-compose.yml"

show_menu() {
  echo "Opções:"
  echo "1. Iniciar os contêineres"
  echo "2. Parar os contêineres"
  echo "3. Ver o status dos contêineres"
  echo "4. Sair"
  read -p "Escolha uma opção: " option
}

password() {
  read -p "Digite a senha para o usuário root do MySQL: " MYSQL_ROOT_PASSWORD

  export MYSQL_ROOT_PASSWORD="$MYSQL_ROOT_PASSWORD"
}

while true; do
  show_menu

  case $option in
    1)
      password
      docker-compose -f "$COMPOSE_FILE" up -d
      echo "Contêineres iniciados."
      ;;
    2)
      password
      docker-compose -f "$COMPOSE_FILE" down
      echo "Contêineres parados."
      ;;
    3)
      password
      docker-compose -f "$COMPOSE_FILE" ps
      ;;
    4)
      break
      ;;
    *)
      echo "Opção inválida."
      ;;
  esac
done