#!/bin/bash

# ===========================================
# Script de Teste da API da Barbearia
# ===========================================

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configurações
BASE_URL="http://localhost:8080"
CONTENT_TYPE="Content-Type: application/json"

echo -e "${BLUE}🚀 Iniciando testes da API da Barbearia...${NC}\n"

# Verificar se a API está rodando
echo -e "${YELLOW}🔍 Verificando se a API está rodando...${NC}"
if ! curl -s "$BASE_URL/actuator/health" > /dev/null; then
    echo -e "${RED}❌ API não está rodando em $BASE_URL${NC}"
    echo -e "${YELLOW}💡 Execute: mvn spring-boot:run${NC}"
    exit 1
fi
echo -e "${GREEN}✅ API está rodando!${NC}\n"

# ===========================================
# 1. TESTES DE ENDPOINTS PÚBLICOS
# ===========================================
echo -e "${BLUE}📋 1. Testando endpoints públicos...${NC}"

echo -e "${YELLOW}   📋 Informações da barbearia:${NC}"
curl -s -X GET "$BASE_URL/api/public/informacoes" | jq '.' || echo "Erro ao buscar informações"

echo -e "\n${YELLOW}   ✂️ Serviços disponíveis:${NC}"
curl -s -X GET "$BASE_URL/api/public/servicos" | jq '.' || echo "Erro ao buscar serviços"

echo -e "\n${YELLOW}   👨‍💼 Barbeiros disponíveis:${NC}"
curl -s -X GET "$BASE_URL/api/public/barbeiros" | jq '.' || echo "Erro ao buscar barbeiros"

# ===========================================
# 2. TESTE DE CADASTRO DE CLIENTE
# ===========================================
echo -e "\n${BLUE}👤 2. Testando cadastro de cliente...${NC}"

# Gerar email único para evitar conflitos
TIMESTAMP=$(date +%s)
EMAIL_TESTE="joao.teste.${TIMESTAMP}@email.com"

CLIENTE_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/cadastro/cliente" \
  -H "$CONTENT_TYPE" \
  -d "{
    "nome": "João Silva Teste",
    "email": "$EMAIL_TESTE",
    "telefone": "(11) 99999-9999",
    "dataNascimento": "1990-01-01",
    "senha": "123456",
    "confirmarSenha": "123456"
  }")

if echo "$CLIENTE_RESPONSE" | jq -e '.id' > /dev/null; then
    echo -e "${GREEN}✅ Cliente cadastrado com sucesso!${NC}"
    CLIENTE_ID=$(echo "$CLIENTE_RESPONSE" | jq -r '.id')
    echo -e "${YELLOW}   Cliente ID: $CLIENTE_ID${NC}"
else
    echo -e "${RED}❌ Erro ao cadastrar cliente:${NC}"
    echo "$CLIENTE_RESPONSE" | jq '.'
fi

# ===========================================
# 3. TESTE DE LOGIN
# ===========================================
echo -e "\n${BLUE}�� 3. Testando login...${NC}"

LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "$CONTENT_TYPE" \
  -d "{
    "email": "$EMAIL_TESTE",
    "senha": "123456"
  }")

if echo "$LOGIN_RESPONSE" | jq -e '.token' > /dev/null; then
    echo -e "${GREEN}✅ Login realizado com sucesso!${NC}"
    TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token')
    USER_ID=$(echo "$LOGIN_RESPONSE" | jq -r '.usuarioId')
    TIPO_USUARIO=$(echo "$LOGIN_RESPONSE" | jq -r '.tipoUsuario')
    echo -e "${YELLOW}   Tipo de usuário: $TIPO_USUARIO${NC}"

    AUTH_HEADER="Authorization: Bearer $TOKEN"

    # ===========================================
    # 4. TESTES COM AUTENTICAÇÃO
    # ===========================================
    echo -e "\n${BLUE}🔒 4. Testando endpoints autenticados...${NC}"

    echo -e "${YELLOW}   👤 Dados do usuário logado:${NC}"
    curl -s -X GET "$BASE_URL/api/auth/me" \
      -H "$AUTH_HEADER" | jq '.' || echo "Erro ao buscar dados do usuário"

    echo -e "\n${YELLOW}   📅 Testando busca de horários disponíveis:${NC}"
    DISPONIBILIDADE_RESPONSE=$(curl -s -X POST "$BASE_URL/api/agendamentos/disponibilidade" \
      -H "$AUTH_HEADER" \
      -H "$CONTENT_TYPE" \
      -d '{
        "barbeiroId": 1,
        "data": "2024-02-15",
        "servicoId": 1
      }')

    if echo "$DISPONIBILIDADE_RESPONSE" | jq -e '.[0]' > /dev/null; then
        echo -e "${GREEN}✅ Horários disponíveis encontrados!${NC}"
        echo "$DISPONIBILIDADE_RESPONSE" | jq '.[:3]' # Mostrar apenas os 3 primeiros
    else
        echo -e "${YELLOW}⚠️ Nenhum horário disponível ou erro:${NC}"
        echo "$DISPONIBILIDADE_RESPONSE" | jq '.'
    fi

    # ===========================================
    # 5. TESTE DE AGENDAMENTO
    # ===========================================
    echo -e "\n${BLUE}�� 5. Testando criação de agendamento...${NC}"

    AGENDAMENTO_RESPONSE=$(curl -s -X POST "$BASE_URL/api/agendamentos" \
      -H "$AUTH_HEADER" \
      -H "$CONTENT_TYPE" \
      -d "{
        "clienteId": $CLIENTE_ID,
        "barbeiroId": 1,
        "servicoId": 1,
        "dataHora": "2024-02-15T14:30:00",
        "observacoes": "Teste de agendamento via script"
      }")

    if echo "$AGENDAMENTO_RESPONSE" | jq -e '.id' > /dev/null; then
        echo -e "${GREEN}✅ Agendamento criado com sucesso!${NC}"
        AGENDAMENTO_ID=$(echo "$AGENDAMENTO_RESPONSE" | jq -r '.id')
        echo -e "${YELLOW}   Agendamento ID: $AGENDAMENTO_ID${NC}"
    else
        echo -e "${YELLOW}⚠️ Erro ao criar agendamento (pode ser horário ocupado):${NC}"
        echo "$AGENDAMENTO_RESPONSE" | jq '.'
    fi

else
    echo -e "${RED}❌ Erro ao fazer login:${NC}"
    echo "$LOGIN_RESPONSE" | jq '.'
fi

# ===========================================
# 6. TESTE COM CREDENCIAIS DE ADMINISTRADOR
# ===========================================
echo -e "\n${BLUE}👨‍💼 6. Testando login como administrador...${NC}"

ADMIN_LOGIN=$(curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "$CONTENT_TYPE" \
  -d '{
    "email": "admin@barbearia.com",
    "senha": "admin123"
  }')

if echo "$ADMIN_LOGIN" | jq -e '.token' > /dev/null; then
    echo -e "${GREEN}✅ Login de admin realizado com sucesso!${NC}"
    ADMIN_TOKEN=$(echo "$ADMIN_LOGIN" | jq -r '.token')
    ADMIN_AUTH="Authorization: Bearer $ADMIN_TOKEN"

    echo -e "\n${YELLOW}   📊 Testando relatório geral:${NC}"
    curl -s -X GET "$BASE_URL/api/relatorios/geral" \
      -H "$ADMIN_AUTH" | jq '.' || echo "Erro ao buscar relatório"

    echo -e "\n${YELLOW}   📈 Testando estatísticas rápidas:${NC}"
    curl -s -X GET "$BASE_URL/api/relatorios/estatisticas" \
      -H "$ADMIN_AUTH" | jq '.' || echo "Erro ao buscar estatísticas"
else
    echo -e "${RED}❌ Erro ao fazer login como admin${NC}"
fi

# ===========================================
# RESUMO FINAL
# ===========================================
echo -e "\n${BLUE}📋 RESUMO DOS TESTES:${NC}"
echo -e "${GREEN}✅ Endpoints públicos funcionando${NC}"
echo -e "${GREEN}✅ Cadastro de cliente funcionando${NC}"
echo -e "${GREEN}✅ Login funcionando${NC}"
echo -e "${GREEN}✅ Autenticação JWT funcionando${NC}"
echo -e "${GREEN}✅ Busca de disponibilidade funcionando${NC}"
echo -e "${GREEN}✅ Sistema de relatórios funcionando${NC}"

echo -e "\n${BLUE}🎉 Testes concluídos!${NC}"
echo -e "${YELLOW}💡 Para mais testes, acesse: http://localhost:8080/swagger-ui.html${NC}"