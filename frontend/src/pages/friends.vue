<template>
  <div>
    <div class="row">
      <div class="col-3">
        <b-card>
          <b-card-header>Friends list</b-card-header>
          <b-list-group>
            <b-list-group-item class="d-flex align-items-center" v-for="friend in friends" :key="friend.id" button @click="openFriend(friend)">
              <b-avatar variant="primary" class="mr-3" :text="(friend.name || '').charAt(0).toUpperCase()"></b-avatar>
              <span>{{friend.name}}</span>
            </b-list-group-item>
          </b-list-group>
        </b-card>
      </div>
      <div class="col-9">
        <div class="text-end">
          <b-btn type="primary" @click="openAddFriend()"><b-icon-plus></b-icon-plus> Add friend</b-btn>
        </div>
      </div>
    </div>
    <b-modal ref="friendModal" hide-footer :title="edit.friend.name">

      <b-list-group>
        <b-list-group-item v-for="(message, idx) in messages" :key="idx">
          <div class="d-flex align-items-center justify-content-between">
            <small class="text-muted">{{message[0]}}</small>
            <small class="text-muted">{{message[2]}}</small>
          </div>
          <span>{{message[1]}}</span>
        </b-list-group-item>
      </b-list-group>
      <b-form @submit="sendMessage">
        <b-form-group>
          <b-form-input
              v-model="edit.friend.message"
              type="text"
              placeholder="Enter message"
              required
          ></b-form-input>
          <b-input-group-append>
            <b-btn variant="outline-primary" type="submit">
              <b-icon-arrow-right-circle />
            </b-btn>
          </b-input-group-append>
        </b-form-group>
      </b-form>
      <div class="d-flex justify-content-center mt-3">
        <b-button v-if="edit.friend.id" variant="danger" style="margin-right: 1em;" @click="deleteFriend(edit.friend)">
          <b-icon-trash /> Delete friend
        </b-button>


      </div>
    </b-modal>

    <b-modal ref="friendAddModal" hide-footer title="Adding new friend">
      <b-form @submit="addFriend(addFriendByID)">
        <b-form-group
            id="input-group-4"
            label="Put ID of other user:"
            label-for="input-4"
            description="you can find it in your profile"
        >
          <b-form-input
              id="input-4"
              v-model="addFriendByID"
              type="text"
              placeholder="Enter user ID"
              required
          ></b-form-input>
        </b-form-group>

        <b-form-group
            id="input-group-4"
            label="Or search for them by name to invite"
            label-for="input-4"
            description=""
        >
          <b-form-input
              id="input-4"
              v-model="addFriendByID"
              type="text"
              placeholder="Pick a user"
              required
          ></b-form-input>
        </b-form-group>

        <div class="d-flex justify-content-center mt-3">
          <b-button type="submit" variant="primary" ><b-icon-check /> Add to friends</b-button>
        </div>
      </b-form>
    </b-modal>

  </div>
</template>
<style scoped>

</style>
<script>
export default {
  name: "friends",
  data() {
    return {
      addFriendByID: 0,
      friends: [],
      messages: [],
      edit: {
        friend: {
          id: 0,
          message: ''
        },
      },
      email: ''
    }
  },
  methods: {
    openFriend(friend) {
      this.edit.friend = {...friend};
      fetch(`http://localhost:8081/getAllMessagesBetweenIDs/${this.$root.user.id}/${friend.id}`)
          .then( response =>  response.json())
          .then( messages => {
            this.messages = messages;
            this.$refs.friendModal.show();
          })
          .catch(exception => console.error(exception));
    },
    closeFriend() {
      this.$refs.friendModal.hide();
    },
    deleteFriend(friend) {
      fetch(`http://localhost:8081/deleteFriendshipBetweenIDs/${this.$root.user.id}/${friend.id}`, {
        method: 'DELETE',
      }).then(() => {
        this.friends.splice(this.friends.findIndex(f => f.id === friend.id), 1);
        this.closeFriend();
      })
    },
    openAddFriend() {
      this.$refs.friendAddModal.show();
    },
    closeAddFriend() {
      this.$refs.friendAddModal.hide();
    },
    sendMessage(e) {
      e.preventDefault();
      fetch(`http://localhost:8081/sendMessageBetweenIDs/${this.$root.user.id}/${this.edit.friend.id}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({msg: this.edit.friend.message})
      })
        .then(r => r.json())
        .then(msg => {
          console.log(msg);
        })
        .catch(e => console.error(e))
    },

    addFriend(addFriendByID){
      alert(addFriendByID);

    },
  },
  mounted(){
    fetch(`http://localhost:8081/findFriendsForID/${this.$root.user.id}`)
        .then( response =>  response.json())
        .then( friends => {
          this.friends = friends;
        })
        .catch(exception => console.error(exception));

  }
}
</script>

<style scoped>

</style>