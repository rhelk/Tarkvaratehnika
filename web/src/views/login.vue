<template>
  <div>
    <h2>Login</h2>
    <form>
      <div class="form-group">
        <label>Email</label>
        <input type="text" v-model="input.username" class="form-control" />
        <!--<div v-show="submitted && !input.username" class="invalid-feedback">Username is required</div>-->
      </div>
      <div class="form-group">
        <label type="password">Password</label>
        <input type="password" v-model="input.password" class="form-control" />
        <!--<div v-show="submitted && !input.password" class="invalid-feedback">Password is required</div>-->
      </div>
      <div class="form-group">
        <button class="btn btn-primary" v-on:click.prevent="loginPreCheck">Login</button>
        <router-link to="/register" class="btn btn-link">Register</router-link>
      </div>
    </form>
  </div>
</template>

<script>

  export default {
    data() {
      return {
        input: {
          username: "",
          password: ""
        },
        submitted: false
      }
    },
    methods: {
      loginPreCheck: function () {
        this.submitted = true;
        if (this.input.username && this.input.password)
          this.login();
      },
      login: function () {
        let username = this.input.username;
        let password = this.input.password;
        // console.log(username, password);
        this.$store.dispatch('login', {username, password})
          .then(() => this.$router.push('/'))
          .catch(err => console.log(err))
      }
    }
  }
</script>

<style scoped>

</style>
