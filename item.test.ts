// Self-contained contract for ONE item folder. GENERATED: edit tests/item.test.template.ts and run `bun run sync-tests`.
// It imports nothing outside this folder, so it keeps working when the folder is mirrored to its own repo.
import { describe, expect, test } from 'bun:test'
import { existsSync, readFileSync, readdirSync } from 'node:fs'
import { extname, join } from 'node:path'

const dir = import.meta.dir
const meta = JSON.parse(readFileSync(join(dir, 'item.json'), 'utf8')) as Record<string, any>
const TAG_AXES = ['gesture', 'pattern', 'effect', 'element'] as const
const CODE_EXT = new Set(['.swift', '.kt', '.kts', '.xml', '.ts', '.tsx', '.js', '.jsx', '.gradle', '.java', '.m', '.h', '.metal', '.md'])
const isPair = (r: unknown) => Array.isArray(r) && r.length === 2 && typeof r[0] === 'string' && typeof r[1] === 'string'
const isPackage = Boolean(meta.steps?.length || meta.artifact || meta.contains)

function unbalanced(src: string): string | null {
  const open = '([{', close: Record<string, string> = { ')': '(', ']': '[', '}': '{' }
  const stack: string[] = []
  let i = 0
  const line = (at: number) => src.slice(0, at).split('\n').length
  while (i < src.length) {
    const c = src[i]
    if (c === '/' && src[i + 1] === '/') { while (i < src.length && src[i] !== '\n') i++; continue }
    if (c === '/' && src[i + 1] === '*') {
      const e = src.indexOf('*/', i + 2)
      if (e < 0) return `unterminated block comment at line ${line(i)}`
      i = e + 2; continue
    }
    if (c === '"') {
      if (src.startsWith('"""', i)) {
        const e = src.indexOf('"""', i + 3)
        if (e < 0) return `unterminated string at line ${line(i)}`
        i = e + 3; continue
      }
      i++
      while (i < src.length && src[i] !== '"' && src[i] !== '\n') { if (src[i] === '\\') i++; i++ }
      i++; continue
    }
    if (c === "'" && (src[i + 2] === "'" || (src[i + 1] === '\\' && src[i + 3] === "'"))) { i += src[i + 1] === '\\' ? 4 : 3; continue }
    if (open.includes(c)) stack.push(c)
    else if (c in close) {
      if (stack.pop() !== close[c]) return `unexpected "${c}" at line ${line(i)}`
    }
    i++
  }
  return stack.length ? `unclosed "${stack[stack.length - 1]}"` : null
}


describe(`item ${meta.slug}`, () => {
  test('item.json has the required fields', () => {
    for (const k of ['id', 'slug', 'title', 'kind', 'desc']) {
      expect(typeof meta[k], k).toBe('string')
      expect(meta[k].trim().length, `${k} is empty`).toBeGreaterThan(0)
    }
    expect(meta.id).toMatch(/^\d{3}$/)
    expect(meta.slug).toMatch(/^[a-z0-9]+(?:-[a-z0-9]+)*$/)
    expect(meta.desc.length, 'desc should stay a single short sentence').toBeLessThanOrEqual(140)
  })

  test('tags cover the four axes', () => {
    expect(meta.tags, 'add "tags": { gesture, pattern, effect, element }').toBeDefined()
    for (const axis of TAG_AXES) {
      const values: string[] = meta.tags?.[axis]
      expect(Array.isArray(values), `tags.${axis} must be an array`).toBe(true)
      for (const v of values ?? []) {
        expect(typeof v).toBe('string')
        expect(v.trim(), `tags.${axis} has an untrimmed value`).toBe(v)
        expect(v.length).toBeGreaterThan(0)
        expect(v.length).toBeLessThanOrEqual(32)
      }
      expect(new Set(values).size, `tags.${axis} has duplicates`).toBe((values ?? []).length)
    }
    // Gesture can be empty (ambient motion); the other three always describe the item.
    for (const axis of ['pattern', 'effect', 'element'] as const)
      expect(meta.tags?.[axis]?.length, `tags.${axis} needs at least one value`).toBeGreaterThan(0)
  })

  test('has a README', () => {
    expect(existsSync(join(dir, 'README.md'))).toBe(true)
  })

  test('repo is an https link when set', () => {
    if (meta.repo) expect(meta.repo).toMatch(/^https:\/\/\S+$/)
  })

  test('listed sources exist, are not empty and are code files', () => {
    for (const s of meta.sources ?? []) {
      expect(typeof s.tab).toBe('string')
      expect(existsSync(join(dir, s.file)), `missing source ${s.file}`).toBe(true)
      expect(readFileSync(join(dir, s.file), 'utf8').trim().length, `${s.file} is empty`).toBeGreaterThan(0)
      expect(CODE_EXT.has(extname(s.file)), s.file).toBe(true)
    }
  })

  test('Swift and Kotlin sources are well formed', () => {
    const files = readdirSync(dir).filter((f) => ['.swift', '.kt'].includes(extname(f)))
    for (const f of files) {
      const code = readFileSync(join(dir, f), 'utf8')
      expect(unbalanced(code), `${f}`).toBeNull()
      if (f.endsWith('.swift') && f !== 'Usage.swift') expect(code, `${f} should import a framework`).toMatch(/^\s*import\s+\w+/m)
      if (f.endsWith('.kt')) expect(code, `${f} should declare a function, class or object`).toMatch(/\b(fun|class|object|interface)\s+\w+/)
    }
  })

  test('detail rows are [label, text] pairs', () => {
    for (const key of ['stats', 'spec', 'params', 'meta'])
      for (const row of meta[key] ?? []) expect(isPair(row), `${key} row ${JSON.stringify(row)}`).toBe(true)
    for (const [t, code] of meta.steps ?? []) {
      expect(typeof t).toBe('string')
      expect(code === null || typeof code === 'string').toBe(true)
    }
  })

  test('optional fields are well formed', () => {
    if (meta.added !== undefined) expect(Number.isNaN(Date.parse(String(meta.added))), 'added must be an ISO date-time').toBe(false)
    if (meta.hidden !== undefined) expect(typeof meta.hidden, 'hidden must be true or false').toBe('boolean')
    if (meta.inspiration !== undefined) {
      expect(Array.isArray(meta.inspiration), 'inspiration must be an array').toBe(true)
      for (const x of meta.inspiration) {
        const e = typeof x === 'string' ? { title: x } : x
        expect(typeof e.title === 'string' && e.title.trim().length > 0, 'every inspiration entry needs a title').toBe(true)
        if (e.url !== undefined) expect(e.url, 'inspiration url must be https').toMatch(/^https:\/\//)
        if (e.note !== undefined) expect(typeof e.note, 'inspiration note must be a string').toBe('string')
      }
    }
    if (meta.posterAt !== undefined) expect(Number.isFinite(meta.posterAt) && meta.posterAt >= 0, 'posterAt must be seconds, 0 or more').toBe(true)
    for (const k of ['video', 'poster']) if (meta[k] !== undefined) expect(meta[k], `${k} must be an https URL`).toMatch(/^https:\/\//)
  })

  test('no media files in the folder (demos live in the media bucket)', () => {
    for (const f of ['demo.mp4', 'demo.webm', 'poster.jpg', 'demo.gif'])
      expect(existsSync(join(dir, f)), `${f} belongs in the media bucket, not here`).toBe(false)
    const gifs = readdirSync(dir).filter((f) => f.endsWith('.gif') || f.endsWith('.mp4'))
    expect(gifs, 'demos belong in the media bucket').toEqual([])
  })

  if (isPackage) {
    test('package explains how to install it', () => {
      expect(Boolean(meta.steps?.length || meta.artifact), 'add "steps" or "artifact"').toBe(true)
    })
  } else {
    test('interaction does not use package-only fields', () => {
      expect(meta.contains).toBeUndefined()
      expect(meta.steps).toBeUndefined()
    })
  }
})
