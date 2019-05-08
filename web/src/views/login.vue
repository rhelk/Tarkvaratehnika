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
               v-validate="'required|email'"
               v-bind:class="{error: loginError}">
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
               v-validate="'required'"
               v-bind:class="{error: loginError}">
        <span v-show="errors.has('password')" class="text-danger">{{ errors.first('password') }}</span>
      </div>
      <b-button style="margin-left: 15px" variant="dark" v-on:click.prevent="validateBeforeSubmit">Login</b-button>
      <router-link to="/register" class="btn btn-link" style="color: black">Register</router-link>
    </form>
    <p v-if="loginError" class="error">Bad login information!</p>
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
        loginError: false
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
        const that = this;
        // console.log(username, password);
        this.$store.dispatch('login', {
          username: this.input.username,
          password: sha.sha256(this.input.password)
        })
          .then((resp) => {
            this.$router.push('/')
          })
          .catch((err) => {
            console.log(err);
            that.loginError = true;
            // that.input.username = "";
            // that.input.password = ""
          })
      }
    }
  }
</script>

<style scoped>
  input.error {
    background-color: #db3646d4;
  }
  p.error {
    color: #db3646d4;
    margin-top: 10px;
  }

</style>
