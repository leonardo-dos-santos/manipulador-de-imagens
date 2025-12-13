# Manipulador de Imagens

Aplicação para manipulação de imagens com front-end em React/Vite/Tailwind e back-end em Java com `HttpServer`.

## Estrutura
- `front-end/`: aplicação React
- `back-end/`: servidor Java e lógica de imagem

## Desenvolvimento
- Back-end: `javac -d back-end/out back-end/src/main/java/com/image/processing/*.java && java -cp back-end/out com.image.processing.SimpleImageServer`
- Front-end: `cd front-end && npm install && npm run dev`

## Endpoints
- `POST /api/process/negative`
- `POST /api/process/invert`
- `POST /api/process/stretch-h`
- `POST /api/process/shrink-v`
- `POST /api/process/color-filter?red&green&blue`
- `POST /api/process/trim?pixelCount`
- `GET /api/health`

