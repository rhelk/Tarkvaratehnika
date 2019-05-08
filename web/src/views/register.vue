<template>
  <div class="container">
    <h2 style="margin: 12px">Register new account</h2>
    <form>
      <div class="form-group col-md-8">
        <label for="formFirstName">First Name:</label>
        <input type="text"
               name="First name"
               class="form-control"
               id="formFirstName"
               placeholder="First name"
               v-model="user.firstName"
               v-validate="'required|min:2'">
        <span v-show="errors.has('First name')" class="text-danger">{{ errors.first('First name') }}</span>
      </div>
      <div class="form-group col-md-8">
        <label for="formLastName">Last Name:</label>
        <input type="text"
               name="Last name"
               class="form-control"
               id="formLastName"
               placeholder="Last name"
               v-model="user.lastName"
               v-validate="'required|min:2'">
        <span v-show="errors.has('Last name')" class="text-danger">{{ errors.first('Last name') }}</span>
      </div>
      <div class="form-group col-md-8">
        <label for="formUsername">Email:</label>
        <input type="text"
               name="email"
               class="form-control"
               id="formUsername"
               placeholder="ex: example@example.com"
               v-model="user.username"
               v-validate="'required|email'"
               v-bind:class="{error: registerError}">
        <span v-show="errors.has('email')" class="text-danger">{{ errors.first('email') }}</span>
        <p v-if="registerError" class="error">This email already exists!</p>
      </div>
      <div class="form-group col-md-8">
        <label for="formPassword">Password:</label>
        <input type="password"
               name="password"
               class="form-control"
               id="formPassword"
               placeholder="Password"
               v-model="user.password"
               v-validate="'required|min:8'">
        <span v-show="errors.has('password')" class="text-danger">{{ errors.first('password') }}</span>
      </div>
      <b-button style="margin-left: 15px" variant="dark" :disabled="errors.any()" type="submit" v-on:click.prevent="validateBeforeSubmit" >Register</b-button>
      <router-link to="/login" class="btn btn-link" style="color: black">Cancel</router-link>
    </form>
  </div>
</template>

<script>
  import sha from 'js-sha256'

  export default {
    data () {
      return {
        user: {
          firstName: '',
          lastName: '',
          username: '',
          password: ''
        },
        registerError: false
      }
    },
    methods: {
      validateBeforeSubmit() {
        this.$validator.validateAll().then((result) => {
          if (result)
            this.register()
        });
      },
      register() {
        const that = this;
        this.$store.dispatch('register', {
          first_name: this.user.firstName,
          last_name: this.user.lastName,
          username: this.user.username,
          password: sha.sha256(this.user.password)
        })
          .then(() => {
            this.$router.push('/')
          })
          .catch(() => {
            that.registerError = true;
          })

      }
    }
  };
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