function performSearch() {
    const query = document.getElementById('search-input').value;


    if (query.length > 0) {

        fetch(`http://localhost:8085/game-boot/game/search?name=${query}`)
            .then(response => response.json())
            .then(data => {

                displaySearchResults(data);
            });
    } else {

        displaySearchResults([]);
    }
}

function displaySearchResults(results) {

    const resultsContainer = document.getElementById('search-results');


    resultsContainer.innerHTML = '';


    if (results.length > 0) {
        results.forEach(result => {
            const resultItem = document.createElement('div');
            const link = document.createElement('a');
            resultItem.onmouseover = function (){
                this.style.backgroundColor = "rgba(224, 222, 232, 0.75)"
            }
            resultItem.onmouseleave = function (){
                this.style.backgroundColor = "white"
            }

            let gameText;
            if (result.gameDetail.price === 0) {
                gameText = `${result.name}<br/>Free to use`;
            } else {
                // Якщо ціна гри більше 0, встановлюємо текст з ціною та символом ₴
                gameText = `${result.name}<br/>${result.gameDetail.price}₴`;
            }

            //TODO: GAME PHOTO

            link.innerHTML = gameText;
            link.href = `http://localhost:8085/game-boot/game/${result.gameId}`; // Створюємо URL для переходу на сторінку гри

            resultItem.appendChild(link);

            resultsContainer.appendChild(resultItem);
        });
    }
}