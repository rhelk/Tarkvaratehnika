<template>
    <div>
        <div class="container" style="margin-top: 20px">
            <h1>Users who request renting:</h1>
            <div class="row">
                <div class="col-md-5">
                    <div v-for="obj in rentObjects" style="margin-top: 30px">
                        <div v-if="myUserId === obj.owner_id">
                            <router-link v-bind:to="/room/ + obj.property.property_id"><h3
                                    style="margin-left: 10px; color: black">
                                {{obj.property.title}}</h3>
                            </router-link>
                            <router-link v-bind:to="/room/ + obj.property.property_id">
                                <img :src="obj.property.pic_url" class="img-thumbnail">
                            </router-link>
                            <div class="caption">
                                <p style="margin-left: 10px; margin-top: 5px">
                                    <b>Details:</b><br>
                                    Contact renter at: {{obj.renter_username}}<br>
                                    Requested period: {{obj.start}} to {{obj.end}}<br>
                                </p>
                                <p v-if="obj.state === 'TO_RENT'" style="margin-left: 10px">
                                    <b-button variant="dark" v-on:click.prevent="confirmRequest(obj.rent_id)">Confirm
                                    </b-button>
                                    <b-button variant="dark" v-on:click.prevent="denyRequest(obj.rent_id)"
                                              style="margin-left: 15px">Deny
                                    </b-button>
                                </p>
                                <p v-if="obj.state === 'CONFIRM_RENT'" style="margin-left: 10px">
                                    <b>Status: Your have confirmed this rent request</b>
                                </p>
                                <p v-if="obj.state === 'DENY_RENT'" style="margin-left: 10px">
                                    <b>Status: Your have denied this rent request</b>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                rentObjects: [],
                myUserId: this.$store.getters.getUser_id,

            }
        },
        created() {
            this.myUserId = this.$store.getters.getUser_id;
            const that = this;
            this.$store.dispatch('doGet', {url: 'rent'}).then(data => {
                that.rentObjects = data.data;
                console.log(data.data)
            })

        },
        beforeCreate() {
            if (!this.$store.getters.isLoggedIn) {
                this.$router.push({path: `/login`});
                this.$router.go();

            }
        },
        methods: {
            confirmRequest: function (rent_id) {
                console.log(rent_id);
                this.$store.dispatch('doPost', {
                    url: 'rent/confirm/' + rent_id

                }).then(data => {
                    console.log("tehtud");
                    this.$store.dispatch('doGet', {url: 'rent'}).then(data => {
                        this.rentObjects = data.data;
                    })
                })
            },

            denyRequest: function (rent_id) {
                this.$store.dispatch('doPost', {
                    url: 'rent/deny/' + rent_id

                }).then(data => {
                    console.log("tehtud");
                    this.$store.dispatch('doGet', {url: 'rent'}).then(data => {
                        this.rentObjects = data.data;
                    })
                })
            },
        }
    }
</script>
<style>

    a {
        color: black;
        text-decoration: none;
        padding: 6px 8px;
    }

    h3 {
        font-family: Arial;
    }
</style>