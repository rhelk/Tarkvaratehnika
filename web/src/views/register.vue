<template>
  <div>
    <h2>Register</h2>
    <form>
      <div class="form-group">
        <label>First name</label>
        <input type="text" v-model="user.firstName" class="form-control" />
        <!--<div v-if="submitted && errors.has('firstName')" class="invalid-feedback">{{ errors.first('firstName') }}</div>-->
      </div>
      <div>
        <label>Last name</label>
        <input type="text" v-model="user.lastName" class="form-control"/>
        <!--<div v-if="submitted && errors.has('lastName')" class="invalid-feedback">{{ errors.first('lastName') }}</div>-->
      </div>
      <div class="form-group">
        <label>Email</label>
        <input type="email" v-model="user.username" class="form-control"/>
        <!--<div v-if="submitted && errors.has('username')" class="invalid-feedback">{{ errors.first('username') }}</div>-->
      </div>
      <div class="form-group">
        <label htmlFor="password">Password</label>
        <input type="password" v-model="user.password" class="form-control"/>
        <!--<div v-if="submitted && errors.has('password')" class="invalid-feedback">{{ errors.first('password') }}</div>-->
      </div>
      <div class="form-group">
        <button class="btn btn-primary" v-on:click="register">Register</button>
        <router-link to="/login" class="btn btn-link">Cancel</router-link>
      </div>
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
      handleSubmit(e) {
        this.submitted = true;
        this.$validator.validate().then(valid => {
          if (valid) {
            this.register(this.user);
          }
        });
      },
      register() {
        this.$store.dispatch('register', {
          first_name: this.user.firstName,
          last_name: this.user.lastName,
          username: this.user.username,
          password: sha.sha256(this.user.password)
        })
          .then(this.$router.push('/'))
          .catch(err => console.log(err))

      }
    }
  };
</script>
