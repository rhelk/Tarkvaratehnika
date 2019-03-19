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
        search: "",
        price: ""

      }
    },
    created() {
      this.$http.get('http://localhost:8080/api/properties').then(function (data) {
        this.rooms = data.body;
      })
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
