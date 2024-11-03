<script lang="ts">
  import { path, distance } from "$lib/requestHandler";
  import { assets } from "$app/paths";
  import places from "$lib/places.json";
</script>

<div
  class="flex flex-col gap-4 h-[800px] overflow-y-auto py-5 px-10 rounded-3xl bg-opacity-40 bg-black"
>
  {#if $path.length == 0}
    <p class="text-3xl font-bold">Wala pang path yet, bro.</p>
  {:else}
    <p class="text-sm font-mono font-light">Start</p>
    {#each $path as name}
      <div class="flex gap-2 items-center">
        <div
          class="aspect-square w-[125px] h-[125px] rounded-full overflow-hidden flex justify-center items-center
      border-black border-2 border-solid first:border-green-300 last:border-green-800"
        >
          <img
            class="max-h-full max-w-full object-contain"
            src="{assets}/logos/{places.find((p) => p.label == name)
              ?.value}.png"
            alt="Logo of {name}"
          />
        </div>
        <span class="text-2xl font-bold">{name}</span>
      </div>
    {/each}
    <p class="text-sm font-mono font-light">End</p>
    <p class="text-3xl font-bold">
      Total distance covered: {$distance.toFixed(2).toLocaleString()}m
    </p>
  {/if}
</div>
