<template>
  <b-navbar>
    <div class="container-fluid">
      <a class="navbar-brand">RentDeck</a>
      <b-navbar>
        <b-nav-form>
          <b-form-input class="mr-sm-2 " type="text" placeholder="Search"/>
          <b-button variant="dark" class="my-2 my-sm-0" type="submit">Search</b-button>
        </b-nav-form>
      </b-navbar>
      <b-navbar-nav class="ml-auto">
        <router-link to='/' exact> Home</router-link>
        <router-link to='/property/add' exact> Add property</router-link>
      </b-navbar-nav>
    </div>
  </b-navbar>
</template>

<script>

  export default {}
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
