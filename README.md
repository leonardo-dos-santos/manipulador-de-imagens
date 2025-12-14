# Manipulador de Imagens

Aplicação web para manipulação de imagens com filtros como negativo, inversão, grayscale, etc.

## Demo

![Demonstração da aplicação](docs/manipulador-de-imagem.png)

## Tecnologias

- **Front-end**: React 18, TypeScript, Vite, Tailwind CSS
- **Back-end**: Java 21, HttpServer (sem frameworks externos)
- **Build**: Maven, Node.js

## Como executar

### Back-end
```bash
cd back-end
mvn exec:java

### Front-end
```bash
cd front-end
npm install
npm run dev
```

## Funcionalidades
- Upload de imagem ou URL
- Aplicar filtros: negativo, inverter, esticar horizontal, reduzir vertical, filtro de cor, cortar bordas, grayscale
- **Back-end**: Maven para build e execução
- **Front-end**: Vite para desenvolvimento rápido com HMR
- **CORS**: Configurado para desenvolvimento local
