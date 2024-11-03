import { writable } from "svelte/store";
import { PUBLIC_PORT_NUMBER } from "$env/static/public";

export const path = writable<string[]>([]);
export const distance = writable<number>(0);

export async function request(
  algorithm: string,
  start: string,
  taste: number,
  price: number,
  ambiance: number,
  location: number,
  service: number
) {
  const url = "http://localhost:" + PUBLIC_PORT_NUMBER;
  const res = await fetch(url, {
    method: "POST",
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
  path.set(data.array);
  distance.set(data.total);
}

path.subscribe((value) => {
  console.log(value);
});
