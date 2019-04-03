<template>
  <div class="container">
    <form>
      <div class="form-group col-md-4">
        <label for="formFristName">First Name:</label>
        <input type="text"
               name="First name"
               class="form-control"
               id="formFristName"
               placeholder="First name"
               v-model="user.firstName"
               v-validate="'required|min:2'">
        <span v-show="errors.has('First name')" class="text-danger">{{ errors.first('First name') }}</span>
      </div>
      <div class="form-group col-md-4">
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
      <div class="form-group col-md-4">
        <label for="formUsername">Email:</label>
        <input type="text"
               name="email"
               class="form-control"
               id="formUsername"
               placeholder="ex: example@example.com"
               v-model="user.username"
               v-validate="'required|email'">
        <span v-show="errors.has('email')" class="text-danger">{{ errors.first('email') }}</span>
      </div>
      <div class="form-group col-md-4">
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
      <button class="btn btn-primary" :disabled="errors.any()" type="submit" v-on:click.prevent="validateBeforeSubmit" >Register</button>
      <router-link to="/login" class="btn btn-link">Cancel</router-link>
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
        submitted: false
      }
    },
    methods: {
      validateBeforeSubmit() {
        this.$validator.validateAll().then((result) => {
          if (result)
            this.register()
        });      },
      register() {
        this.$store.dispatch('register', {
          first_name: this.user.firstName,
          last_name: this.user.lastName,
          username: this.user.username,
          password: sha.sha256(this.user.password)
        })
          .then((resp) => {
            this.$router.push('/')
          })
          .catch(err => console.log(err))

      }
    }
  };
</script>
