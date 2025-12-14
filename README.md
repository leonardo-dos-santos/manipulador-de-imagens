# Manipulador de Imagens

Aplicação web para manipulação de imagens com filtros como negativo, inversão, grayscale, etc.

## Demo

[Assista ao vídeo demonstrativo](docs/Manipulador-de-imagens.mp4)

## Como executar

### Back-end
```bash
cd back-end
mvn exec:java
```
Servidor roda em http://localhost:8080

### Front-end
```bash
cd front-end
npm install
npm run dev
```
Acesse em http://localhost:5173

## Funcionalidades
- Upload de imagem ou URL
- Aplicar filtros: negativo, inverter, esticar horizontal, reduzir vertical, filtro de cor, cortar bordas, grayscale
- **Back-end**: Maven para build e execução
- **Front-end**: Vite para desenvolvimento rápido com HMR
- **CORS**: Configurado para desenvolvimento local
