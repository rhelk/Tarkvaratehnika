<template>
  <div class="container" style="margin-top: 20px">
    <div class="row">
      <div class="col-md-4" v-for="room in rooms">
        <div class="thumbnail">
          <router-link v-bind:to="/room/ + room.property_id">
            <img :src="room.pic_url" >
            <div class="caption">
              <p><router-link v-bind:to="/room/ + room.property_id"><h2>{{room.title}}</h2></router-link>.</p>
            </div>
          </router-link>
        </div>
      </div>
    </div>


  </div>
</template>

<script>
  export default {
    data() {
      return {
        rooms: [],
        testrooms: [],
        search: "",
        price: "",
        id: this.$route.params.id,

      }
    },
    created() {
      console.log(this.id);
      const that = this
      if(this.id === undefined || this.id === "" ){
      //if(this.id === "all" ){
          console.log("here 2");
          this.$store.dispatch('doGet',{url: "properties"}).then(data => {
            console.log("p2ringus")
            console.log(data)
            that.rooms = data.data;

            console.log("allRooms");
            console.log(that);
          })

      }else{
        console.log("here 1");
        this.$http.get('http://localhost:8080/api/properties/search?address=' + this.id).then(function (data) {
          console.log(data);
          this.rooms = data.body;
          this.id = "";

      })}
      console.log("juba siin")
    },
  }
</script>

<style scoped>

  .thumbnail img {
    height:250px;
    width:100%;
  }

  a {
    color: black;
    text-decoration: none;
    padding: 6px 8px;
  }

</style>
