<template>
  <div>
    <div id="price">
      <h2>Price</h2>
      <p>Price per night: {{roomDetails.price}}</p>
    </div>
    <div id="roomProfile">
      <img :src="roomDetails.pic_url">
    </div>
    <div id="houseDetails">
      <h1>{{roomDetails.title}}</h1>
      <p>Location: {{roomDetails.address}}</p>
      <p>Rooms: {{roomDetails.room_count}}</p>
      <p>Beds: {{roomDetails.bed_count}}</p>
      <p>Description: {{roomDetails.description}}</p>
    </div>
  </div>

</template>

<script>

  export default {
    data: function () {
      return {
        roomDetails: [],
        id: this.$route.params.id,
      }
    },
    created() {

      this.$http.get('http://localhost:8080/api/property/get/' + this.id).then(function (data) {
        this.roomDetails = data.body;
      });

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

  #price {
    align-content: center;
    width: 100%;
    max-width: 400px;
    box-sizing: border-box;
    background: whitesmoke;
    padding: 30px;
    margin: 60px;
    float: right;
  }

</style>
