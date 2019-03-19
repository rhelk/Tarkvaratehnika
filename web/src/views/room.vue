<template>
  <div class=container-fluid style="margin-top: 20px">
    <div class="row">
      <div class="col-md-2"></div>
      <div class="col-md-6">
        <b-img :src="roomDetails.pic_url" fluid alt="Responsive image"/>
      </div>
      <div id="price" class="col-md-4">
        <h2>Price</h2>
        <p>Price per night: {{roomDetails.price}}</p>
      </div>
    </div>
    <div class="row">
      <div class="col-md-2"></div>
      <div id="houseDetails" class="col-md-6">
        <h1>{{roomDetails.title}}</h1>
        <p>Location: {{roomDetails.address}}</p>
        <p>Rooms: {{roomDetails.room_count}}</p>
        <p>Beds: {{roomDetails.bed_count}}</p>
        <p>Description: {{roomDetails.description}}</p>
      </div>
      <div class="col-md-4"></div>
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
    margin: auto;
    padding: 20px;
  }

  #price {
    max-width: 300px;
    background: whitesmoke;
    height: 200px;
    margin-top: 60px;


  }

</style>
