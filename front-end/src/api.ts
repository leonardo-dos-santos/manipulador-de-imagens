export async function processImage(endpoint: string, opts: { file?: File, imageUrl?: string, params?: Record<string, string | number> } = {}) {
  const url = new URL(`http://localhost:8080${endpoint}`)
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

