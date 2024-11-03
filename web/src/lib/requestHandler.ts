import { writable } from "svelte/store";
import { PUBLIC_PORT_NUMBER } from "$env/static/public";

export const path = writable<string[]>([]);

export async function request(
  algorithm: string,
  start: string,
  taste: number,
  price: number,
  ambiance: number,
  location: number,
  service: number
) {
  const url = "localhost:" + PUBLIC_PORT_NUMBER;
  const res = await fetch(url, {
    body: JSON.stringify({
      algorithm,
      start,
      taste,
      price,
      ambiance,
      location,
      service,
    }),
  });
  const data = await res.json();
  path.set(data);
}
