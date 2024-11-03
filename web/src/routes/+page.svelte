<script lang="ts">
  import places from "$lib/places.json";
  import algorithms from "$lib/algorithms.json";
  import {
    Autocomplete,
    type AutocompleteOption,
    RangeSlider,
  } from "@skeletonlabs/skeleton";

  import { request } from "$lib/requestHandler";

  import { slide } from "svelte/transition";

  let searchPlace = "",
    searchAlgo = "",
    taste = 5,
    price = 5,
    ambiance = 5,
    location = 5,
    service = 5;

  function onSelectPlace(event: CustomEvent<AutocompleteOption>) {
    searchPlace = event.detail.label;
  }

  function onSelectAlgo(event: CustomEvent<AutocompleteOption>) {
    searchAlgo = event.detail.label;
  }

  function startAlgo() {
    const algoId = algorithms.find((algo) => algo.label == searchAlgo)!.value;
    const placeId = places.find((place) => place.label == searchPlace)!.value;

    request(algoId, placeId, taste, price, ambiance, location, service);
  }

  $: isInPlaces = places.some((place) => place.label == searchPlace);
  $: isInAlgorithms = algorithms.some(
    (algorithm) => algorithm.label == searchAlgo
  );
  $: isValid = isInPlaces && isInAlgorithms;
</script>

<div class="grid grid-cols-4">
  <form
    on:submit|preventDefault={startAlgo}
    class="flex flex-col gap-5 col-span-2 justify-center min-h-screen items-center transition-all"
  >
    <div class="w-3/5">
      <input
        class="input p-2 rounded-t-md"
        class:rounded-b-none={!isInPlaces}
        class:rounded-b-md={isInPlaces}
        type="search"
        name="searchbar"
        bind:value={searchPlace}
        placeholder="Where are we making kain, pare?"
      />
      {#if !isInPlaces}
        <div
          class="card w-full max-h-48 p-4 overflow-y-auto"
          tabindex="-1"
          transition:slide={{ axis: "y" }}
        >
          <Autocomplete
            class="rounded-t-none"
            bind:input={searchPlace}
            options={places}
            on:selection={onSelectPlace}
          ></Autocomplete>
        </div>
      {/if}
    </div>
    <div class="flex flex-col gap-2 w-[450px]">
      <span class="text-lg">Priorities</span>
      <RangeSlider name="taste" bind:value={taste} max={10} step={1}>
        <div class="flex justify-between items-center">
          <span>Taste</span><span>{taste} / 10</span>
        </div>
      </RangeSlider>
      <RangeSlider name="price" bind:value={price} max={10} step={1}>
        <div class="flex justify-between items-center">
          <span>Price</span><span>{price} / 10</span>
        </div>
      </RangeSlider>
      <RangeSlider name="ambiance" bind:value={ambiance} max={10} step={1}>
        <div class="flex justify-between items-center">
          <span>Ambiance</span><span>{ambiance} / 10</span>
        </div>
      </RangeSlider>
      <RangeSlider name="location" bind:value={location} max={10} step={1}>
        <div class="flex justify-between items-center">
          <span>Location</span><span>{location} / 10</span>
        </div>
      </RangeSlider>
      <RangeSlider name="service" bind:value={service} max={10} step={1}>
        <div class="flex justify-between items-center">
          <span>Service</span><span>{service} / 10</span>
        </div>
      </RangeSlider>
    </div>

    <div class="w-3/5">
      <input
        class="input p-2 rounded-t-md"
        class:rounded-b-none={!isInAlgorithms}
        class:rounded-b-md={isInAlgorithms}
        type="search"
        name="searchbar"
        bind:value={searchAlgo}
        placeholder="How are we making hanap?"
      />
      {#if !isInAlgorithms}
        <div
          class="card w-full max-h-20 p-4 overflow-y-auto"
          tabindex="-1"
          transition:slide={{ axis: "y" }}
        >
          <Autocomplete
            class="rounded-t-none"
            bind:input={searchAlgo}
            options={algorithms}
            on:selection={onSelectAlgo}
          ></Autocomplete>
        </div>
      {/if}
    </div>

    <button
      class="btn"
      class:rainbow-button={isValid}
      disabled={!isValid}
      class:variant-ghost={!isValid}
    >
      {#if isValid}
        <span>Make hanap na!!!!</span>
      {:else}
        <span>Can't make hanap pa.</span>
      {/if}
    </button>
  </form>
</div>

<style>
  .rainbow-button {
    background: linear-gradient(
      90deg,
      red,
      orange,
      yellow,
      green,
      blue,
      indigo,
      violet
    );
    background-size: 400% 400%;
    animation: rainbow 5s ease infinite;
    color: white;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 1); /* Add text shadow */
    cursor: pointer;
  }

  @keyframes rainbow {
    0% {
      background-position: 0% 50%;
    }
    50% {
      background-position: 100% 50%;
    }
    100% {
      background-position: 0% 50%;
    }
  }
</style>
