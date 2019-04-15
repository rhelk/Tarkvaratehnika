<template>
  <div class="container" style="margin-top: 20px">
    <div class="row">
      <div class="col-md-4" v-for="room in rooms">
        <div class="thumbnail">
          <router-link v-bind:to="/room/ + room.property_id">
            <img :src="room.pic_url">
            <div class="caption">
              <p>
                <router-link v-bind:to="/room/ + room.property_id"><h2>{{room.title}}</h2></router-link>
                .
              </p>
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
        price: "",
        id: this.$route.params.id,

      }
    },
    created() {
      this.fetchData();


    },
    watch: {
      '$route': 'fetchData'
    },
    methods:{

      fetchData () {
        this.id = this.$route.params.id;
        console.log("here");
        console.log(this.id);
        const that = this;
        if (this.id === undefined || this.id === "") {
          this.$store.dispatch('doGet', {url: "property/all"}).then(data => {

            // Example, replace 200 and 400 with values you want. Will need some way to access those variables however
            // Maybe can be in data part or something.
            // let filteredData = data.data.filter(single => single.price >= 200 && single.price <= 400);
            // console.log('Filtered is ' + filteredData);
            // console.log('F1 ' + filteredData[0].price + '  F2 ' + filteredData[1].price);

            that.rooms = data.data;
          })

        } else {
          this.$store.dispatch('doGet', {url: 'property/search?municipality=' + this.id}).then(data => {
            that.rooms = data.data;
          })
        }
      }
    }
  }
</script>

<style scoped>

  .thumbnail img {
    height: 250px;
    width: 100%;
  }

  a {
    color: black;
    text-decoration: none;
    padding: 6px 8px;
  }

</style>
