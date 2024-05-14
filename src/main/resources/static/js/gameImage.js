let apiUrl = 'http://localhost:8085/game-boot/v1/game'

function savePhoto(id) {
    let htmlphoto = document.getElementById(`ephoto${id}`)
    const file = htmlphoto.files[0];

    const formData = new FormData();
    formData.append('id', id);
    formData.append('photo', file);

    let reqBody = {
        method: 'POST',
        body: formData
    }

    fetch(`${apiUrl}/upload-photo`, reqBody)
}




