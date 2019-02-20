<template>
  <div>
    <div id="price">
      <h2>Price</h2>
      <p>Price per night: {{details.price}}</p>
    </div>
    <div id="roomProfile">
      <img :src="details.image">
    </div>
    <div id="houseDetails">
      <h1>{{details.name}}</h1>
      <p>Location: {{details.location}}</p>
      <p>Rooms: {{details.rooms}}</p>
      <p>Beds: {{details.beds}}</p>
      <p>Description: {{details.description}}</p>
    </div>
  </div>

</template>

<script>
  import image from "./room.jpg"


  export default {
    data: function () {
      return {
        details:{
          image: image,
          name: 'Best room ever!',
          location: 'Somewhere in EU',
          rooms: 100,
          beds: 7,
          description: 'Located in the heart of sth, near to the center. Very best of the best.',
          price: 1000 + '€',
          roomDetails: []
        }

      }
    },
    created() {
      fetch("").then(function (data) {
        this.details.roomDetails = data;
        //do v-for and add data to fields?
        //v get päring??

      })
    }
  }
</script>

<style scoped>
  #roomProfile {
    align-content: center;
    width: 100%;
    max-width: 1000px;
    box-sizing: border-box;
    margin: auto;
    padding: 20px;
  }

  img {
    width: 90%;
  }

  #houseDetails {
    align-content: center;
    width: 100%;
    max-width: 1000px;
    box-sizing: border-box;
    margin: auto;
    padding: 20px;
  }

  #price{
    align-content: center;
    width: 100%;
    max-width: 400px;
    box-sizing: border-box;
    background: whitesmoke;
    padding: 30px;
    margin: 60px;
    float:right;
  }

</style>
