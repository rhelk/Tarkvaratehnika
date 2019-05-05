<template>
  <div>
    <div class="container-fluid">
      <div class="row">
        <div style="margin-left: 25px; margin-top: 10px">
          <p>Price</p>
        </div>
        <div style="margin-top: 15px" class="col-md-2">
          <vue-slider v-model="value"
                      :order="false"
                      v-bind="options"
                      @change="filterByPrice"/>
        </div>

      </div>
    </div>
    <div class="container" style="margin-top: 20px">
      <div class="row">
        <div class="col-lg-4 col-md-6 col-xs-6"  v-for="room in rooms">
          <div class="thumbnail">
            <router-link v-bind:to="/room/ + room.property_id">
              <img class="img-responsive" :src="room.pic_url">
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


  </div>
</template>


<script>
  import VueSlider from 'vue-slider-component'
  import 'vue-slider-component/theme/antd.css'

  export default {
    components: {
      VueSlider
    },
    data() {
      return {
        options: {
          max: 2000,
        },
        value: [0, 2000],
        rooms: [],
        rooms2: [],
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
    methods: {

      fetchData() {
        this.id = this.$route.params.id;
        const that = this;
        if (this.id === undefined || this.id === "") {
          this.$store.dispatch('doGet', {url: "property/all"}).then(data => {

            that.rooms = data.data;
            that.rooms2 = data.data;
          })

        } else {
          this.$store.dispatch('doGet', {url: 'property/search?municipality=' + this.id}).then(data => {
            that.rooms = data.data;
          })
        }
      },
      filterByPrice() {
        this.rooms = this.rooms2;
        let filteredData = this.rooms.filter(single => single.price >= this.value[0] && single.price <= this.value[1]);
        this.rooms = filteredData;
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
