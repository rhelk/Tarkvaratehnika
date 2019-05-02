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
        <!--<a href="/all">All properties</a>-->
        <router-link  to='/all' exact> All properties </router-link>
        <router-link v-if="isAuthenticated" to='/property/add' exact> Add property</router-link>
        <router-link v-if="isAuthenticated" to='/my/rooms' exact> My properties</router-link>
        <router-link v-if="isAuthenticated" to='/user' exact> My rent requests</router-link>
        <router-link v-if="!isAuthenticated" to="/login" replace>Login</router-link>
        <router-link v-if="isAuthenticated" to="/login" v-on:click.native="logout()" replace>Logout</router-link>
      </b-navbar-nav>
    </div>
  </b-navbar>
</template>

<script>

  export default {
    data() {
      return {
        search: "",
      }
    },
    methods: {

      searchProperties: function () {
        this.$store.dispatch('doGet', {url: 'property/search?municipality=' + this.search}).then(data => {
          if(data.data.length !== 0){
            this.$router.push({path: `/all/${this.search}`});
            //this.$router.go();
          }
          else{
            //do nothing
          }
        })
      },
      logout: function () {
        this.$store.dispatch('logout')
          .then(() => {
            this.$router.push('/login')
          })
      }
    },
    computed: {
      isAuthenticated() {
        return this.$store.getters.isLoggedIn;
      }
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
