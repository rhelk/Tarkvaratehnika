<template>
  <b-navbar>
    <div class="container-fluid">
      <a class="navbar-brand">RentDeck</a>
      <b-navbar>
        <b-nav-form>
          <b-form-input v-model="search" class="mr-sm-2 " type="text" placeholder="Search"/>
          <b-button variant="dark" class="my-2 my-sm-0" type="submit" v-on:click.prevent="searchProperties">Search</b-button>
        </b-nav-form>
      </b-navbar>
      <b-navbar-nav class="ml-auto">
        <router-link  to='/' exact> Home </router-link>
        <a href="/all">All properties</a>
        <router-link to='/property/add' exact> Add property</router-link>
      </b-navbar-nav>
    </div>
  </b-navbar>
</template>

<script>

  //<router-link  to='/all' exact> All properties</router-link>>

  export default {
    data() {
      return {
        search: "",
      }
    },
    methods: {
      searchProperties: function () {
        this.$http.get(`http://localhost:8080/api/properties/search?address=${this.search}`).then(function (data) {
          if(data.body.length !== 0) {
            this.$router.push({path: `/all/${this.search}`});
            this.$router.go();
          }
          else{
            //do nothing

          }

        })},
    }
  }
</script>

<style scoped>

  a {
    color: black;
    text-decoration: none;
    padding: 6px 8px;
  }

  nav {
    border-bottom: 1px solid whitesmoke;
  }

</style>
