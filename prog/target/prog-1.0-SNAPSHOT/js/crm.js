const hamburger = document.getElementById("hamburger");
hamburger.addEventListener("click", function(){
    const sidebar = document.getElementsByClassName("sidebar")[0];
    const content = document.getElementsByClassName("content")[0];
    sidebar.classList.toggle("collapse");
    content.classList.toggle("full-width");
});


const homeImg = document.getElementById("logo1");
homeImg.addEventListener("click", function() {
    window.location.href = "/prog_war/crm/home";
});
