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
        <b-button v-if="userCanRent" v-bind:disabled="hasClicked" variant="dark" class="my-2 my-sm-0" type="submit" v-on:click.prevent="rentProperty">Rent</b-button>
      </div>
    </div>
    <div class="row">
      <div class="col-md-2"></div>
      <div id="houseDetails" class="col-md-6">
        <h1>{{roomDetails.title}}</h1>
        <p>Location: {{roomDetails.county  + ", " +  roomDetails.municipality  + ", " + roomDetails.settlement  + ", " + roomDetails.street }}</p>
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
        userCanRent: true,
        hasClicked: false,
        roomDetails: [],
        id: this.$route.params.id,
      }
    },
    created() {
      const that = this;
      this.$store.dispatch('doGet', {url: 'property/get/' + this.id}).then(data => {
        that.roomDetails = data.data;
        if(data.data.users.user_id === this.$store.getters.getUser_id){
          that.userCanRent=false;
          console.log(that.userCanChange)
        }
      })


    },
    methods: {
      rentProperty: function () {
        this.hasClicked = true;
        if (!this.$store.getters.isLoggedIn) {
          this.$router.push({path: `/login`});
          this.$router.go();
        }else{
        const that = this;
        this.$store.dispatch('doPost', {
          url: 'property/rent/', body:
              this.id,

        }).then(data => {
          console.log("tehtud");
          this.$router.push({path: `/all`});
          //this.$router.go();
        })}


      }
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
