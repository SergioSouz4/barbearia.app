#!/bin/bash

# ===========================================
# Script de Inicialização da Aplicação
# ===========================================

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}�� Iniciando aplicação da Barbearia...${NC}\n"

# Verificar se o Java está instalado
if ! command -v java &> /dev/null; then
    echo -e "${YELLOW}⚠️ Java não encontrado. Instale o Java 17+ primeiro.${NC}"
    exit 1
fi

# Verificar se o Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo -e "${YELLOW}⚠️ Maven não encontrado. Instale o Maven primeiro.${NC}"
    exit 1
fi

echo -e "${YELLOW}📦 Compilando aplicação...${NC}"
mvn clean compile

echo -e "\n${YELLOW}🏃‍♂️ Iniciando aplicação...${NC}"
echo -e "${BLUE}📱 A aplicação estará disponível em: http://localhost:8080${NC}"
echo -e "${BLUE}📚 Documentação da API: http://localhost:8080/swagger-ui.html${NC}"
echo -e "\n${YELLOW}💡 Para parar a aplicação, pressione Ctrl+C${NC}\n"

mvn spring-boot:run