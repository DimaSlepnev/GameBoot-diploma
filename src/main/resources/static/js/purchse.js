document.getElementById('buy-now').addEventListener('click', function() {
    // Відкриття модального вікна
    $('#purchaseModal').modal('show');

   let userBalance = document.getElementById('user-balance').textContent;
    let gamePrice = document.getElementById('game-price').textContent;
    console.log(userBalance)
    console.log(gamePrice)

    // Перевірка балансу
    if (userBalance < gamePrice) {
        // Якщо баланс недостатній, відображаємо попередження та кнопку поповнення балансу
        document.getElementById('balance-warning').style.display = 'block';
        document.getElementById('confirm-purchase').disabled = true;
    } else {
        // Якщо баланс достатній, дозволяємо підтвердити покупку
        document.getElementById('balance-warning').style.display = 'none';
        document.getElementById('confirm-purchase').disabled = false;
    }
});