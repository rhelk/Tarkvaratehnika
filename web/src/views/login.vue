<template>
  <div class="container">
    <h2 style="margin: 12px">Login</h2>
    <form>
      <div class="form-group col-md-8">
        <label for="formUsername">Email:</label>
        <input type="text"
               name="email"
               class="form-control"
               id="formUsername"
               placeholder="ex: example@example.com"
               v-model="input.username"
               v-validate="'required'">
        <span v-show="errors.has('email')" class="text-danger">{{ errors.first('email') }}</span>
      </div>
      <div class="form-group col-md-8">
        <label for="formPassword">Password:</label>
        <input type="password"
               name="password"
               class="form-control"
               id="formPassword"
               placeholder="Password"
               v-model="input.password"
               v-validate="'required'">
        <span v-show="errors.has('password')" class="text-danger">{{ errors.first('password') }}</span>
      </div>
      <b-button style="margin-left: 15px" variant="dark" v-on:click.prevent="validateBeforeSubmit">Login</b-button>
      <router-link to="/register" class="btn btn-link" style="color: black">Register</router-link>
    </form>
  </div>
</template>

<script>
  import sha from 'js-sha256'


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
      validateBeforeSubmit() {
        this.$validator.validateAll().then((result) => {
          if (result)
            this.login()
        });
      },
      login() {
        // console.log(username, password);
        this.$store.dispatch('login', {
          username: this.input.username,
          password: sha.sha256(this.input.password)
        })
          .then((resp) => {
            this.$router.push('/')
          })
          .catch(err => {
            console.log(err);
          })
      }
    }
  }
</script>

<style scoped>


</style>
