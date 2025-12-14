import { useRef, useState } from 'react'
import { processImage } from './api'

export default function App() {
  const [file, setFile] = useState<File | null>(null)
  const [imageUrl, setImageUrl] = useState('')
  const [outputUrl, setOutputUrl] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)
  const [red, setRed] = useState(0)
  const [green, setGreen] = useState(0)
  const [blue, setBlue] = useState(0)
  const [trim, setTrim] = useState(10)

  const sourcePreview = file ? URL.createObjectURL(file) : (imageUrl || null)

  async function run(endpoint: string, params?: Record<string, string | number>) {
    setLoading(true)
    try {
      const url = await processImage(endpoint, { file: file ?? undefined, imageUrl: file ? undefined : imageUrl, params })
      setOutputUrl(url)
    } catch (e) {
      console.error(e)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex flex-col">
      <header className="p-4 bg-white shadow">
        <h1 className="text-xl font-semibold">Manipulador de Imagens</h1>
      </header>
      <main className="flex-1 p-4 grid grid-cols-1 lg:grid-cols-3 gap-4">
        <div className="bg-white rounded-lg shadow p-4">
          <div className="space-y-3">
            <div className="flex items-center gap-3">
              <input
                id="file-input"
                ref={useRef<HTMLInputElement>(null)}
                type="file"
                accept="image/*"
                onChange={e => setFile(e.target.files?.[0] ?? null)}
                className="hidden"
              />
              <label htmlFor="file-input">
                <span className="px-3 py-2 bg-gray-800 text-white rounded cursor-pointer">Selecionar arquivo</span>
              </label>
              <span className="text-sm text-gray-600">{file ? file.name : 'Nenhum arquivo selecionado'}</span>
            </div>
            <input
              type="url"
              value={imageUrl}
              onChange={e => setImageUrl(e.target.value)}
              placeholder="URL da imagem"
              className="w-full border rounded px-3 py-2"
            />
            <div className="grid grid-cols-3 gap-2">
              <div>
                <label className="block text-sm">Vermelho</label>
                <input type="number" value={red} onChange={e => setRed(parseInt(e.target.value || '0'))} className="w-full border rounded px-2 py-1" />
              </div>
              <div>
                <label className="block text-sm">Verde</label>
                <input type="number" value={green} onChange={e => setGreen(parseInt(e.target.value || '0'))} className="w-full border rounded px-2 py-1" />
              </div>
              <div>
                <label className="block text-sm">Azul</label>
                <input type="number" value={blue} onChange={e => setBlue(parseInt(e.target.value || '0'))} className="w-full border rounded px-2 py-1" />
              </div>
            </div>
            <div>
              <label className="block text-sm">Pixels de corte</label>
              <input type="number" value={trim} onChange={e => setTrim(parseInt(e.target.value || '0'))} className="w-full border rounded px-2 py-1" />
            </div>
            <div className="flex flex-wrap gap-2">
              <button className="px-3 py-2 bg-gray-800 text-white rounded" disabled={loading} onClick={() => run('/api/process/negative')}>Negativo</button>
              <button className="px-3 py-2 bg-gray-800 text-white rounded" disabled={loading} onClick={() => run('/api/process/invert')}>Inverter</button>
              <button className="px-3 py-2 bg-gray-800 text-white rounded" disabled={loading} onClick={() => run('/api/process/stretch-h')}>Esticar Horizontal</button>
              <button className="px-3 py-2 bg-gray-800 text-white rounded" disabled={loading} onClick={() => run('/api/process/shrink-v')}>Reduzir Vertical</button>
              <button className="px-3 py-2 bg-gray-800 text-white rounded" disabled={loading} onClick={() => run('/api/process/color-filter', { red, green, blue })}>Filtro de Cor</button>
              <button className="px-3 py-2 bg-gray-800 text-white rounded" disabled={loading} onClick={() => run('/api/process/trim', { pixelCount: trim })}>Cortar Bordas</button>
              <button className="px-3 py-2 bg-gray-800 text-white rounded" disabled={loading} onClick={() => run('/api/process/grayscale')}>Tons de cinza</button>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-lg shadow p-4 flex items-center justify-center">
          {sourcePreview ? (
            <img src={sourcePreview} className="max-h-[70vh] object-contain" />
          ) : (
            <div className="text-gray-500">Selecione um arquivo ou URL</div>
          )}
        </div>
        <div className="bg-white rounded-lg shadow p-4 flex items-center justify-center">
          {outputUrl ? (
            <img src={outputUrl} className="max-h-[70vh] object-contain" />
          ) : (
            <div className="text-gray-500">Resultado aparecerá aqui</div>
          )}
        </div>
      </main>
    </div>
  )
}

