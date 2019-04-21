    <template>
        <div class="container-fluid">
            <div class="row justify-content-center">
                <div class="col-xl-7 col-lg-8 col-md-9 col-sm-11">
        <div class="post-list-item my-2 p-2 text-left" v-for="data in response" :key='data.id'>
            <p>Filename: {{data.filename}}</p>
            <p>Format: {{data.formatting}}</p>
            <p>Height: {{data.height}}</p>
            <p>Width: {{data.width}}</p>
            <p>Category: {{data.category}}</p>
            <img v-bind:src="data.downloadUri" />
        </div>
        <form id="post-form" @submit.prevent="postFormData">
            <div class="form-group text-left">
                <label for="fileUpload">File:</label><br/>
                <label class="custom-file-upload" for="fileUpload">
                    Choose a file
                </label>
                <input type="file" class="form-control-file" id="fileUpload" @change="loadTextFromFile">
                <p>
                    <small>
                        Max file size: 20MB <br/>
                        Allowed file types:  .png, .jpg, .jpeg, .tiff
                    </small>
                </p>

            </div>
            <input class="btn btn-lg btn-primary mb-3" type="submit" value="Submit">
        </form>
                    <form id="postUrl-form" @submit.prevent="postUrlData">
                    <textarea class="form-control" id="exampleFormControlTextarea1" rows="3" name="url" placeholder="URL"
                              v-model="url"></textarea>
                        <input class="btn btn-lg btn-primary mb-3" type="submit" value="Submit">
                    </form>
                </div>
            </div>
        </div>
</template>
<script>
    import apiRequests from './../javascript/apiRequests.js';
    import errorHandling from './../javascript/errorHandling.js';
    export default  {

        name: 'uploadfile',
        data() {
            return {

                response: [],
                file_location: '',
                url: '',
                file: null
            };
        },
        methods: {
            loadTextFromFile(input) {
                this.file = input.target.files[0];
            },
            resetFields() {

                this.file_location = '';
                this.url = '';
                this.file = null;

            },

            postFormData() {
                const formData = new FormData();
                formData.append('file', this.file);
                apiRequests
                    .postRequestToApi('/api/uploadFile', formData)
                    .then((response) => {
                        this.file_location = response.data.fileDownloadUri;
                        this.dataRequest();


                    })
                    .catch(() => {
                            errorHandling.errorMsgWithButton("There was a problem uploading Your file!" +
                                "Check the limitations!")
                        }
                    );
                this.resetFields();
            },
            postUrlData() {
                const formData = new FormData();
                formData.append('url', this.url);
                apiRequests
                    .postRequestToApi('/api/uploadFileUrl', formData)
                    .then((response) => {
                        this.file_location = response.data.fileDownloadUri;
                        this.dataRequest();

                    })
                    .catch(() => {
                            errorHandling.errorMsgWithButton("There was a problem uploading Your file!" +
                                "Check the limitations!")
                        }
                    );
                this.resetFields();
            },

            dataRequest() {
                apiRequests.getRequestToApi('/api/allUploads')
                    .then(result => {
                        this.response = result.data;

                    });
            },


        },
        mounted() {

            this.dataRequest();
        }
    }
    </script>
<style scoped>
    .custom-file-upload {
        border: 2px solid gray;
        color: gray;
        background-color: white;
        padding: 8px 20px;
        border-radius: 8px;
        font-size: 20px;
        font-weight: bold;
    }

    input[type=file] {
        display: none;
    }
</style>


