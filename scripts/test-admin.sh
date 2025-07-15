#!/bin/bash

# ===========================================
# Script de Teste - Funcionalidades Admin
# ===========================================

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

BASE_URL="http://localhost:8080"
CONTENT_TYPE="Content-Type: application/json"

echo -e "${BLUE}👨‍💼 Testando funcionalidades de Administrador...${NC}\n"

# Login como admin
echo -e "${YELLOW}🔐 Fazendo login como administrador...${NC}"
ADMIN_LOGIN=$(curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "$CONTENT_TYPE" \
  -d '{
    "email": "admin@barbearia.com",
    "senha": "admin123"
  }')

if echo "$ADMIN_LOGIN" | jq -e '.token' > /dev/null; then
    echo -e "${GREEN}✅ Login de admin realizado!${NC}"
    ADMIN_TOKEN=$(echo "$ADMIN_LOGIN" | jq -r '.token')
    AUTH_HEADER="Authorization: Bearer $ADMIN_TOKEN"

    # Teste de criação de serviço
    echo -e "\n${YELLOW}💼 Criando novo serviço...${NC}"
    SERVICO_RESPONSE=$(curl -s -X POST "$BASE_URL/api/servicos" \
      -H "$AUTH_HEADER" \
      -H "$CONTENT_TYPE" \
      -d '{
        "nome": "Hidratação Capilar",
        "descricao": "Tratamento de hidratação para cabelos",
        "preco": 45.00,
        "duracaoMinutos": 60
      }')

    if echo "$SERVICO_RESPONSE" | jq -e '.id' > /dev/null; then
        echo -e "${GREEN}✅ Serviço criado com sucesso!${NC}"
        SERVICO_ID=$(echo "$SERVICO_RESPONSE" | jq -r '.id')
        echo -e "${YELLOW}   Serviço ID: $SERVICO_ID${NC}"
    else
        echo -e "${RED}❌ Erro ao criar serviço${NC}"
    fi

    # Teste de relatórios
    echo -e "\n${YELLOW}📊 Testando relatórios...${NC}"

    echo -e "   📈 Relatório geral:"
    curl -s -X GET "$BASE_URL/api/relatorios/geral" \
      -H "$AUTH_HEADER" | jq '.'

    echo -e "\n   💰 Relatório financeiro:"
    curl -s -X GET "$BASE_URL/api/relatorios/financeiro?inicio=2024-01-01&fim=2024-12-31" \
      -H "$AUTH_HEADER" | jq '.'

    echo -e "\n   📋 Dashboard completo:"
    curl -s -X GET "$BASE_URL/api/relatorios/dashboard" \
      -H "$AUTH_HEADER" | jq '.'

    # Teste de comissões
    echo -e "\n${YELLOW}💰 Testando comissões...${NC}"
    curl -s -X GET "$BASE_URL/api/comissoes/periodo?inicio=2024-01-01&fim=2024-12-31" \
      -H "$AUTH_HEADER" | jq '.'

else
    echo -e "${RED}❌ Erro ao fazer login como admin${NC}"
    exit 1
fi

echo -e "\n${GREEN}✅ Testes de admin concluídos!${NC}"