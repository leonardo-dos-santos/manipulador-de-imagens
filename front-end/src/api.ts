export async function processImage(endpoint: string, opts: { file?: File, imageUrl?: string, params?: Record<string, string | number> } = {}) {
  const base = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
  const url = new URL(endpoint, base)
  if (opts.params) {
    Object.entries(opts.params).forEach(([k, v]) => url.searchParams.set(k, String(v)))
  }
  if (opts.imageUrl) {
    url.searchParams.set('imageUrl', opts.imageUrl)
  }
  const body = opts.file ?? undefined
  const res = await fetch(url.toString(), {
    method: 'POST',
    body
  })
  if (!res.ok) throw new Error('Falha ao processar imagem')
  const blob = await res.blob()
  return URL.createObjectURL(blob)
}
