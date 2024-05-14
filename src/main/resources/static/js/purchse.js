document.getElementById('buy-now').addEventListener('click', function() {
    $('#purchaseModal').modal('show');

   let userBalance = document.getElementById('user-balance').textContent;
    let gamePrice = document.getElementById('game-price').textContent;


    if (userBalance < gamePrice) {
        document.getElementById('balance-warning').style.display = 'block';
        document.getElementById('confirm-purchase').disabled = true;
    } else {
        document.getElementById('balance-warning').style.display = 'none';
        document.getElementById('confirm-purchase').disabled = false;
    }
});