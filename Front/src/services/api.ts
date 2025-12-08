export async function apiFetch(input: RequestInfo, init?: RequestInit): Promise<Response> {
  const usuarioStr = typeof window !== 'undefined' ? localStorage.getItem('usuario') : null
  const headers = new Headers(init && init.headers ? init.headers : undefined)

  if (usuarioStr) {
    try {
      const u = JSON.parse(usuarioStr)
      if (u && u.matricula) {
        headers.set('X-User-Matricula', u.matricula)
      }
    } catch (e) {
      // ignore
    }
  }

  const merged: RequestInit = {
    ...init,
    headers
  }

  return fetch(input, merged)
}

export default apiFetch
