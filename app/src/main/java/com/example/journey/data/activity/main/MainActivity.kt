package com.example.journey.data.activity.main

import SearchFragment
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.journey.R
import com.example.journey.data.activity.Roulette.RouletteActivity
import com.example.journey.data.activity.schedule.ScheduleActivity
import com.example.journey.data.activity.cafe.CafeActivity
import com.example.journey.data.activity.calc.DivisionCalculate
import com.example.journey.data.activity.rest.RestActivity
import com.example.journey.data.activity.share.ImageshareActivity
import com.example.journey.data.remote.model.cafe.KakaoPlace
import com.example.journey.data.activity.schedule.ScheduleActivity
import com.example.journey.data.remote.Token
import com.example.journey.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    private var backPressedTime: Long = 0
    private lateinit var naverMap: NaverMap
    private var requestLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val nickname = it.data?.getStringExtra("name")
        val email = it.data?.getStringExtra("email")

        binding.nickname.text = nickname
        binding.email.text = email
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLat: Double = 0.0
    private var currentLng: Double = 0.0
    private var currentMarker: Marker? = null
    private val markers = mutableListOf<Marker>()
    private val infoWindow = InfoWindow()

    private fun moveToLocation(lat: Double, lng: Double, name: String?) {
        val coord = LatLng(lat, lng)

        currentMarker?.map = null

        currentMarker = Marker().apply {
            position = coord
            captionText = name ?: ""
            map = naverMap
        }

        val cameraUpdate = CameraUpdate.scrollAndZoomTo(coord, 16.0)
            .animate(CameraAnimation.Fly)

        naverMap.moveCamera(cameraUpdate)
    }

    private fun displayTopFivePlaces(places: List<KakaoPlace>) {
        markers.forEach { it.map = null }
        markers.clear()


        val sortedPlaces = places.sortedBy {
            val dx = it.x.toDouble() - currentLng
            val dy = it.y.toDouble() - currentLat
            dx * dx + dy * dy
        }.take(5)

        sortedPlaces.forEach { place ->
            val lat = place.y.toDoubleOrNull() ?: return@forEach
            val lng = place.x.toDoubleOrNull() ?: return@forEach
            val marker = Marker().apply {
                position = LatLng(lat, lng)
                captionText = place.name
                map = naverMap
            }

            marker.setOnClickListener {
                showPlaceInfo(marker, place)
                true
            }

            markers.add(marker)
        }

        sortedPlaces.firstOrNull()?.let {
            val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                LatLng(it.y.toDouble(), it.x.toDouble()),
                15.0
            ).animate(CameraAnimation.Easing)
            naverMap.moveCamera(cameraUpdate)
        }
    }

    private fun showPlaceInfo(marker: Marker, place: KakaoPlace) {
        infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this) {
            override fun getText(infoWindow: InfoWindow): CharSequence {
                return "${place.name}\n${place.roadAddress}\n전화: ${place.phone}"
            }
        }
        if (infoWindow.marker == marker) {
            infoWindow.close()
        } else {
            infoWindow.open(marker)
        }
    }


    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없으면 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLat = location.latitude
                currentLng = location.longitude

                val userLocation = LatLng(currentLat, currentLng)
                val update = CameraUpdate.scrollAndZoomTo(userLocation, 15.0)
                    .animate(CameraAnimation.Easing)
                naverMap.moveCamera(update)
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toggle = ActionBarDrawerToggle(this,binding.drawer,
            R.string.drawer_open,
            R.string.drawer_close
        )
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.clearMarkersButton.isEnabled = false

        var isExpanded = false
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync{ map ->
            naverMap = map
            getCurrentLocation()
            if (currentLat != 0.0 && currentLng != 0.0) {
                val location = LatLng(currentLat, currentLng)
                val cameraUpdate = CameraUpdate.scrollTo(location)
                naverMap.moveCamera(cameraUpdate)
            }
        }
        fun dpToPx(dp: Float): Float {
            return dp * Resources.getSystem().displayMetrics.density
        }

        binding.handlewrapper.setOnClickListener {
            binding.root.post {
                val screenHeight = binding.root.height
                val moveDistance = screenHeight * 0.65f - dpToPx(150f)
                Log.d("SlideAnim", "screenHeight = $screenHeight")
                Log.d("SlideAnim", "moveDistance = $moveDistance")

                val translationY = if (isExpanded) 0f else moveDistance
                val targetAlpha = if (isExpanded) 1f else 0f

                binding.clearMarkersButton.visibility = if (isExpanded) View.GONE else View.VISIBLE

                binding.clearMarkersButton.isEnabled = true

                binding.topbackimage.animate()
                    .alpha(targetAlpha)
                    .setDuration(200)
                    .withEndAction {
                        if(isExpanded) {
                            binding.topbackimage.visibility = View.VISIBLE
                        }
                        else{
                            binding.topbackimage.visibility = View.GONE
                        }
                    }
                    .start()


                binding.moveframe.animate()
                    .translationY(translationY)
                    .setDuration(300)
                    .withEndAction {
                        isExpanded = !isExpanded
                    }
                    .start()


            }
        }

        supportFragmentManager.setFragmentResultListener("place_result", this) { _, bundle ->
            val name = bundle.getString("name")
            val lat = bundle.getDouble("lat")
            val lng = bundle.getDouble("lng")
            moveToLocation(lat, lng, name)
        }

        supportFragmentManager.setFragmentResultListener("place_list_result", this) { _, bundle ->
            val places = bundle.getParcelableArrayList<KakaoPlace>("places")
            Log.d("MainDebug", "받은 장소 수: ${places?.size}")
            places?.let {
                displayTopFivePlaces(it)
            }
        }

        binding.search.setOnClickListener {
            val fragment = SearchFragment().apply {
                arguments = Bundle().apply {
                    putDouble("lat", currentLat)
                    putDouble("lng", currentLng)
                }
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.search_fragment_container, fragment)
                .addToBackStack(null)
                .commit()

            findViewById<FrameLayout>(R.id.search_fragment_container).visibility = View.VISIBLE
        }




        binding.mset.setOnClickListener {
            val intent = Intent(this, FixprofileActivity::class.java)
            requestLauncher.launch(intent)
        }

        binding.cafe.setOnClickListener {
            startActivity(Intent(this, CafeActivity::class.java))
        }

        binding.spin.setOnClickListener {
            startActivity(Intent(this, RouletteActivity::class.java))
        }

        binding.timetable.setOnClickListener {
            startActivity(Intent(this, ScheduleActivity::class.java))
        }

        binding.matzip.setOnClickListener {
            startActivity(Intent(this, RestActivity::class.java))
        }

        binding.cal.setOnClickListener {
            startActivity(Intent(this, DivisionCalculate::class.java))
        }

        binding.share.setOnClickListener {
            startActivity(Intent(this, ImageshareActivity::class.java))
        }

        binding.clearMarkersButton.setOnClickListener {
            markers.forEach { it.map = null }
            markers.clear()
            infoWindow.close() // 정보창도 같이 닫기 (선택)
        }


    }

    override fun onResume() {
        super.onResume()
        val pref = Token.appContext.getSharedPreferences("profile", Context.MODE_PRIVATE)

        binding.nickname.text = pref.getString("username", null).toString()
        binding.email.text = pref.getString("useremail", null).toString()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - backPressedTime < 2000) {
            finishAffinity()
        } else {
            backPressedTime = currentTime
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        super.onBackPressed()
    }
}