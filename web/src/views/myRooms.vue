<template>
  <div>

    <div v-if="user_has_properties" class="container" style="margin-top: 20px">
      <div class="row">
        <div class="col-md-4" v-for="room in rooms">
          <div class="thumbnail">
            <router-link v-bind:to="/room/ + room.property_id">
              <img :src="room.pic_url">
            </router-link>
            <div class="caption">
              <p style="margin-top: 20px">
                <router-link v-bind:to="/change/ + room.property_id"><b>
                  <small>Change your property</small>
                </b></router-link>
                <router-link v-bind:to="/room/ + room.property_id"><h2>{{room.title}}</h2></router-link>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="container">
      <h1>You don't have any properties yet</h1>
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
        user_has_properties: true,

      }
    },
    created() {
      const that = this;
      this.$store.dispatch('doGet', {url: 'property/mine'}).then(data => {
        console.log(that.$store.getters.getUser_id);
        if (data.data.length !== 0) {
          that.rooms = data.data;
        } else {
          this.user_has_properties = false;
        }
      })

    },
    beforeCreate() {
      if (!this.$store.getters.isLoggedIn) {
        this.$router.push({path: `/login`});
        this.$router.go();

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

  small {
    text-decoration-line: underline;
    font-size: medium;
  }
</style>
