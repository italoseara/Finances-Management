# Finances-Management

# Objetivos

- [ ] Definir a estrutura do banco de dados
- [ ] Criar o banco de dados caso não exista
- [ ] Popular as tabelas com dados ficticios
- [ ] Criar uma tabela na interface para exibir os dados

# Tabela "Transactions"

id: INTEGER PRIMARY KEY
date: INTEGER (DATETIME)
description: VARCHAR(100)
amount: REAL
category: VARCHAR(100)