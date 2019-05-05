<template>
    <div>

        <div v-if="user_has_properties" class="container" style="margin-top: 20px">
            <b-button variant="dark" style="margin-bottom: 20px" v-on:click.prevent="rentRequests">Rent Requests
            </b-button>
            <div class="row">
                <div class="col-lg-4 col-md-6 col-xs-6" v-for="room in rooms">
                    <div class="thumbnail">
                        <router-link v-bind:to="/room/ + room.property_id">
                            <img :src="room.pic_url">
                        </router-link>
                        <b-button v-if="room.visibility === 'VISIBLE'" style="margin-top: 10px; margin-right: 5px"
                                  variant="dark" v-on:click.prevent="hide(room.property_id)">Deactivate
                        </b-button>
                        <b-button v-if="room.visibility === 'HIDDEN'" style="margin-top: 10px" variant="dark"
                                  v-on:click.prevent="visible(room.property_id)">Make active
                        </b-button>
                        <div class="caption">
                            <p style="margin-top: 20px">
                                <router-link v-bind:to="/change/ + room.property_id"><b>
                                    <small>Change your property</small>
                                </b></router-link>
                                <router-link v-bind:to="/room/ + room.property_id"><h2>{{room.title}}</h2></router-link>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div v-else class="container">
            <h1>You don't have any properties yet</h1>
        </div>

    </div>
</template>

<script>
    export default {
        data() {
            return {
                rooms: [],
                search: "",
                price: "",
                user_has_properties: true,

            }
        },
        created() {
            this.getProperties();

        },

        beforeCreate() {
            if (!this.$store.getters.isLoggedIn) {
                this.$router.push({path: `/login`});
                this.$router.go();

            }
        },

        methods: {
            rentRequests: function () {
                this.$router.push({path: `/rentout/`});
            },

            getProperties: function () {
                const that = this;
                this.$store.dispatch('doGet', {url: 'property/mine'}).then(data => {
                    if (data.data.length !== 0) {
                        that.rooms = data.data;
                    } else {
                        this.user_has_properties = false;
                    }
                })

            },
            hide: function (propertyId) {
                this.$store.dispatch('doPost', {
                    url: 'property/hidden/' + propertyId
                }).then(data => {
                    this.getProperties();
                    this.$router.push({path: `/my/rooms`});
                })
            },
            visible: function (propertyId) {
                this.$store.dispatch('doPost', {
                    url: 'property/visible/' + propertyId
                }).then(data => {
                    this.getProperties()
                    this.$router.push({path: `/my/rooms`});
                })
            }
        }
    }
</script>

<style scoped>

    .thumbnail img {
        height: 250px;
        width: 100%;
    }

    a {
        color: black;
        text-decoration: none;
        padding: 6px 8px;
    }

    small {
        text-decoration-line: underline;
        font-size: medium;
    }
</style>
