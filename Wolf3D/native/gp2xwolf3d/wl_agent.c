#include "wl_def.h"

/*
=============================================================================

						 LOCAL CONSTANTS

=============================================================================
*/

#define MOVESCALE		150
#define BACKMOVESCALE		100
#define ANGLESCALE		20

/*
=============================================================================

						 GLOBAL VARIABLES

=============================================================================
*/

//
// player state info
//
long		thrustspeed;

unsigned	plux, pluy;	// player coordinates scaled to unsigned

int		anglefrac;
int		gotgatgun;

objtype		*LastAttacker;

/*
=============================================================================

						 LOCAL VARIABLES

=============================================================================
*/


static const struct atkinf
{
	signed char tics, attack, frame; 	// attack is 1 for gun, 2 for knife
} attackinfo[4][14] =
{
{ {6,0,1},{6,2,2},{6,0,3},{6,-1,4} },
{ {6,0,1},{6,1,2},{6,0,3},{6,-1,4} },
{ {6,0,1},{6,1,2},{6,3,3},{6,-1,4} },
{ {6,0,1},{6,1,2},{6,4,3},{6,-1,4} },
};

void DrawWeapon();
void GiveWeapon(int weapon);
void GiveAmmo(int ammo);

/*
=============================================================================

						CONTROL STUFF

=============================================================================
*/

/*
======================
=
= CheckWeaponChange
=
= Keys 1-4 change weapons
=
======================
*/

void CheckWeaponChange()
{
	if (!gamestate.ammo)		// must use knife with no ammo
		return;

	if(buttonstate[bt_cycleWeapon] && !buttonheld[bt_cycleWeapon])
	{
		gamestate.weapon++;
		if(gamestate.weapon > gamestate.bestweapon)
			gamestate.weapon = wp_knife;
		gamestate.chosenweapon = gamestate.weapon;
		DrawWeapon();
		return;
	}

        // Cacau

	if(buttonstate[bt_readyknife] && !buttonheld[bt_readyknife])
	{
		gamestate.weapon = wp_knife;
		gamestate.chosenweapon = gamestate.weapon;
		DrawWeapon();
		return;
	}

	if(buttonstate[bt_readypistol] && !buttonheld[bt_readypistol])
	{
		gamestate.weapon = wp_pistol;
		if(gamestate.weapon > gamestate.bestweapon)
			gamestate.weapon = wp_knife;
		gamestate.chosenweapon = gamestate.weapon;
		DrawWeapon();
		return;
	}

	if(buttonstate[bt_readymachinegun] && !buttonheld[bt_readymachinegun])
	{
                int prev = gamestate.bestweapon;
		gamestate.weapon = wp_machinegun;
		if(gamestate.weapon > gamestate.bestweapon)
		        gamestate.weapon = prev;
		gamestate.chosenweapon = gamestate.weapon;
		DrawWeapon();
		return;
	}

	if(buttonstate[bt_readychaingun] && !buttonheld[bt_readychaingun])
	{
                int prev = gamestate.bestweapon;
		gamestate.weapon = wp_chaingun;
		if(gamestate.weapon > gamestate.bestweapon)
                        gamestate.weapon = prev;
		gamestate.chosenweapon = gamestate.weapon;
		DrawWeapon();
		return;
	}

	return;
}

// Cacau

int cheat (int code)
{
   if (gamestate.bestweapon == wp_knife)
      GiveWeapon (wp_pistol)                              ;
   GiveWeapon (wp_machinegun);
   GiveWeapon (wp_chaingun);

   GiveAmmo   (200);

   gamestate.health = 100;
   gamestate.lives  = 5;
   gamestate.keys   = 3;

   DrawKeys();
   DrawWeapon();
   DrawAmmo();
   DrawHealth();
   DrawFace();
   DrawLives();
}

/*
=======================
=
= ControlMovement
=
= Takes controlx,controly, and buttonstate[bt_strafe]
=
= Changes the player's angle and position
=
= There is an angle hack because when going 70 fps, the roundoff becomes
= significant
=
=======================
*/

void ControlMovement(objtype *ob)
{
	int		angle;
	int		angleunits;

	thrustspeed = 0;
//
// side to side move
//

	//else
	{
	//
	// not strafing
	//
		anglefrac += controlx;
		angleunits = anglefrac/ANGLESCALE;
		anglefrac -= angleunits*ANGLESCALE;
		ob->angle -= angleunits;

		if (ob->angle >= ANGLES)
			ob->angle -= ANGLES;
		if (ob->angle < 0)
			ob->angle += ANGLES;

	}


	if (buttonstate[bt_strafe] || buttonstate[bt_strafeLeft] || buttonstate[bt_strafeRight])
	{
	//
	// strafing
	//
	//

		if(buttonstate[bt_strafeLeft])
			controlx = -100;

		if(buttonstate[bt_strafeRight])
			controlx = 100;

		if(buttonstate[bt_strafeLeft] && buttonstate[bt_strafeRight])
			controlx = 0; //hack to stop one strafe over the other

/* Vladimir
jni_printf("ControlMovement Strafe stl=%d, str=%d, controlx=%d\n"
	, buttonstate[bt_strafeLeft], buttonstate[bt_strafeRight], controlx);
*/
		if (controlx > 0)
		{
			angle = ob->angle - ANGLES/4;
			if (angle < 0)
				angle += ANGLES;
			Thrust (angle,controlx*MOVESCALE);	// move to left
//jni_printf("ControlMovement Strafe Left");
		}
		else if (controlx < 0)
		{
			angle = ob->angle + ANGLES/4;
			if (angle >= ANGLES)
				angle -= ANGLES;
			Thrust (angle,-controlx*MOVESCALE);	// move to right
//jni_printf("ControlMovement Strafe Right");
		}

	} else {
	//
	// not strafing
	//
		anglefrac += controlx;
		angleunits = anglefrac/ANGLESCALE;
		anglefrac -= angleunits*ANGLESCALE;
		ob->angle -= angleunits;

		if (ob->angle >= ANGLES)
			ob->angle -= ANGLES;
		if (ob->angle < 0)
			ob->angle += ANGLES;

	}

//
// forward/backwards move
//
	if (controly < 0)
	{
		Thrust (ob->angle,-controly*MOVESCALE);	// move forwards
	}
	else if (controly > 0)
	{
		angle = ob->angle + ANGLES/2;
		if (angle >= ANGLES)
			angle -= ANGLES;
		Thrust (angle,controly*BACKMOVESCALE);		// move backwards
	}

	if (gamestate.victoryflag)		// watching the BJ actor
		return;
}

/*
=============================================================================

					STATUS WINDOW STUFF

=============================================================================
*/


/*
==================
=
= StatusDrawPic
=
==================
*/

void StatusDrawPic(unsigned x, unsigned y, unsigned picnum)
{
	VWB_DrawPic(x*8, y+160, picnum);
}


/*
==================
=
= DrawFace
=
==================
*/

void DrawFace()
{
	if (gamestate.health) {
		if (s0 == true || s1 == true || s2 == true || s3 == true){
			if (godmode)
				StatusDrawPic(17,4,GODMODEFACE1PIC+gamestate.faceframe);
			else
				StatusDrawPic (17,4,FACE1APIC+3*((100-gamestate.health)/16)+gamestate.faceframe);
		} else {
			StatusDrawPic (17,4,FACE1APIC+3*((100-gamestate.health)/16)+gamestate.faceframe);
		}
	} else {
		if (w0 == true || w1 == true){
			if (LastAttacker->obclass == needleobj)
				StatusDrawPic(17,4,MUTANTBJPIC);
			else
				StatusDrawPic (17,4,FACE8APIC);
		} else {
			StatusDrawPic (17,4,FACE8APIC);
		}
	}
}


/*
===============
=
= UpdateFace
=
= Calls draw face if time to change
=
===============
*/

int facecount;

void UpdateFace()
{
	if (w0 == true || w1 == true){
		if (SD_SoundPlaying() == GETGATLINGSNDWL6){
			return;
		}
	} else {
		if (SD_SoundPlaying() == GETGATLINGSNDSOD){
			return;
		}
	}
		

	facecount += tics;
	if (facecount > US_RndT())
	{
		gamestate.faceframe = (US_RndT()>>6);
		if (gamestate.faceframe==3)
			gamestate.faceframe = 1;

		facecount = 0;
		DrawFace();
	}
}


/*
===============
=
= LatchNumber
=
= right justifies and pads with blanks
=
===============
*/

void LatchNumber(int x, int y, int width, long number)
{
	int length, c;

	ltoa(number,str,10);

	length = strlen(str);

	while(length < width)
	{
		StatusDrawPic(x,y,N_BLANKPIC);
		x++;
		width--;
	}

	c = length <= width ? 0 : length-width;

	while(c<length)
	{
		StatusDrawPic(x,y,str[c]-'0'+ N_0PIC);
		x++;
		c++;
	}
}


/*
===============
=
= DrawHealth
=
===============
*/

void DrawHealth()
{
	LatchNumber(21,16,3,gamestate.health);
}


/*
===============
=
= TakeDamage
=
===============
*/

void TakeDamage(int points, objtype *attacker)
{
	LastAttacker = attacker;

	if (gamestate.victoryflag)
		return;
	if (gamestate.difficulty==gd_baby)
	  points>>=2;

	if (!godmode)
		gamestate.health -= points;

	if (gamestate.health<=0)
	{
		gamestate.health = 0;
		playstate = ex_died;
		killerobj = attacker;
	}

	StartDamageFlash(points);

	gotgatgun=0;

	DrawHealth();
	DrawFace();

	//
	// MAKE BJ'S EYES BUG IF MAJOR DAMAGE!
	//
	if (s0 == true || s1 == true || s2 == true || s3 == true){
		if (points > 30 && gamestate.health!=0 && !godmode)
		{
			StatusDrawPic (17,4,BJOUCHPIC);
			facecount = 0;
		}
	}

}


/*
===============
=
= HealSelf
=
===============
*/

void HealSelf(int points)
{
	gamestate.health += points;
	if (gamestate.health>100)
		gamestate.health = 100;

	DrawHealth();
	gotgatgun = 0;	// JR
	DrawFace();
}


//===========================================================================


/*
===============
=
= DrawLevel
=
===============
*/

void DrawLevel()
{
	if (s0 == true || s1 == true || s2 == true || s3 == true){
		if (gamestate.mapon == 20)
			LatchNumber (2,16,2,18);
		else
			LatchNumber(2,16,2,gamestate.mapon+1);
	} else {
		LatchNumber(2,16,2,gamestate.mapon+1);
	}
}

//===========================================================================


/*
===============
=
= DrawLives
=
===============
*/

void DrawLives()
{
	LatchNumber(14,16,1,gamestate.lives);
}


/*
===============
=
= GiveExtraMan
=
===============
*/

void GiveExtraMan()
{
	if (gamestate.lives<9)
		gamestate.lives++;
	DrawLives();
	if (w0 == true || w1 == true){
		SD_PlaySoundWL6(BONUS1UPSNDWL6);
	} else {
		SD_PlaySoundSOD(BONUS1UPSNDSOD);
	}
}

//===========================================================================

/*
===============
=
= DrawScore
=
===============
*/

void DrawScore()
{
	LatchNumber(6, 16, 6, gamestate.score);
}

/*
===============
=
= GivePoints
=
===============
*/

void GivePoints(long points)
{
	gamestate.score += points;
	while (gamestate.score >= gamestate.nextextra)
	{
		gamestate.nextextra += EXTRAPOINTS;
		GiveExtraMan();
	}
	DrawScore();
}

//===========================================================================

/*
==================
=
= DrawWeapon
=
==================
*/

void DrawWeapon()
{
	StatusDrawPic(32,8,KNIFEPIC+gamestate.weapon);
}

/*
==================
=
= DrawKeys
=
==================
*/

void DrawKeys()
{
	if (gamestate.keys & 1)
		StatusDrawPic(30,4,GOLDKEYPIC);
	else
		StatusDrawPic(30,4,NOKEYPIC);

	if (gamestate.keys & 2)
		StatusDrawPic(30,20,SILVERKEYPIC);
	else
		StatusDrawPic(30,20,NOKEYPIC);
}

/*
==================
=
= GiveWeapon
=
==================
*/

void GiveWeapon(int weapon)
{
	GiveAmmo(6);

	if (gamestate.bestweapon<weapon)
		gamestate.bestweapon = gamestate.weapon
		= gamestate.chosenweapon = weapon;

	DrawWeapon();
}

//===========================================================================

/*
===============
=
= DrawAmmo
=
===============
*/

void DrawAmmo()
{
	LatchNumber(27,16,2,gamestate.ammo);
}

/*
===============
=
= GiveAmmo
=
===============
*/

void GiveAmmo(int ammo)
{
	if (!gamestate.ammo)				// knife was out
	{
		if (!gamestate.attackframe)
		{
			gamestate.weapon = gamestate.chosenweapon;
			DrawWeapon ();
		}
	}
	gamestate.ammo += ammo;
	if (gamestate.ammo > 99)
		gamestate.ammo = 99;
	DrawAmmo();
}

//===========================================================================

/*
==================
=
= GiveKey
=
==================
*/

void GiveKey(int key)
{
	gamestate.keys |= (1<<key);
	DrawKeys();
}

/*
=============================================================================

							MOVEMENT

=============================================================================
*/

/*
===================
=
= GetBonus
=
===================
*/
void GetBonus (statobj_t *check)
{
	switch (check->itemnumber)
	{
	case	bo_firstaid:
		if (gamestate.health == 100)
			return;

		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (HEALTH2SNDWL6);
		} else {
			
			SD_PlaySoundSOD (HEALTH2SNDSOD);
		}
		HealSelf (25);
		break;

	case	bo_key1:
	case	bo_key2:
	case	bo_key3:
	case	bo_key4:
		GiveKey (check->itemnumber - bo_key1);
		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (GETKEYSNDWL6);
		} else {
			
			SD_PlaySoundSOD (GETKEYSNDSOD);
		}
		break;

	case	bo_cross:
		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (BONUS1SNDWL6);
		} else {
			SD_PlaySoundSOD (BONUS1SNDSOD);
		}
		GivePoints (100);
		gamestate.treasurecount++;
		break;
	case	bo_chalice:
		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (BONUS2SNDWL6);
		} else {
			SD_PlaySoundSOD (BONUS2SNDSOD);
		}
		GivePoints (500);
		gamestate.treasurecount++;
		break;
	case	bo_bible:
		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (BONUS3SNDWL6);
		} else {
			SD_PlaySoundSOD (BONUS3SNDSOD);
		}
		GivePoints (1000);
		gamestate.treasurecount++;
		break;
	case	bo_crown:
		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (BONUS4SNDWL6);
		} else {
			SD_PlaySoundSOD (BONUS4SNDSOD);
		}
		GivePoints (5000);
		gamestate.treasurecount++;
		break;

	case	bo_clip:
		if (gamestate.ammo == 99)
			return;

		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (GETAMMOSNDWL6);
		} else {
			
			SD_PlaySoundSOD (GETAMMOSNDSOD);
		}
		GiveAmmo (8);
		break;
	case	bo_clip2:
		if (gamestate.ammo == 99)
			return;

		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (GETAMMOSNDWL6);
		} else {
			
			SD_PlaySoundSOD (GETAMMOSNDSOD);
		}
		GiveAmmo (4);
		break;

	case	bo_25clip:
		if (gamestate.ammo == 99)
		  return;

		SD_PlaySoundSOD (GETAMMOBOXSND);
			
		GiveAmmo (25);
		break;

	case	bo_machinegun:
		
		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (GETMACHINESNDWL6);
		} else {
			SD_PlaySoundSOD (GETMACHINESNDSOD);
		}
		GiveWeapon (wp_machinegun);
		break;
	case	bo_chaingun:
		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (GETGATLINGSNDWL6);
		} else {
			SD_PlaySoundSOD (GETGATLINGSNDSOD);
		}
			
		GiveWeapon (wp_chaingun);

		StatusDrawPic (17,4,GOTGATLINGPIC);
		facecount = 0;
		gotgatgun = 1;
		break;

	case	bo_fullheal:
		if (w0 == true || w1 == true){
			SD_PlaySoundWL6(BONUS1UPSNDWL6);
		} else {
			SD_PlaySoundSOD(BONUS1UPSNDSOD);
		}
		HealSelf (99);
		GiveAmmo (25);
		GiveExtraMan ();
		gamestate.treasurecount++;
		break;

	case	bo_food:
		if (gamestate.health == 100)
			return;

		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (HEALTH1SNDWL6);
		} else {
			SD_PlaySoundSOD (HEALTH1SNDSOD);
		}
		HealSelf (10);
		break;

	case	bo_alpo:
		if (gamestate.health == 100)
			return;

		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (HEALTH1SNDWL6);
		} else {
			SD_PlaySoundSOD (HEALTH1SNDSOD);
		}
		HealSelf (4);
		break;

	case	bo_gibs:
		if (gamestate.health >10)
			return;

		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (SLURPIESNDWL6);
		} else {
			SD_PlaySoundSOD (SLURPIESNDSOD);
		}
		HealSelf (1);
		break;

	case	bo_spear:
		spearflag = true;
		spearx = player->x;
		speary = player->y;
		spearangle = player->angle;
		playstate = ex_completed;
	}

	StartBonusFlash ();
	check->shapenum = -1;			// remove from list
}

/*
===================
=
= TryMove
=
= returns true if move ok
=
===================
*/

boolean TryMove(objtype *ob)
{
	int		xl,yl,xh,yh,x,y;
	objtype		*check;
	long		deltax,deltay;

	xl = (ob->x-PLAYERSIZE) >>TILESHIFT;
	yl = (ob->y-PLAYERSIZE) >>TILESHIFT;

	xh = (ob->x+PLAYERSIZE) >>TILESHIFT;
	yh = (ob->y+PLAYERSIZE) >>TILESHIFT;

//
// check for solid walls
//
	for (y=yl;y<=yh;y++)
		for (x=xl;x<=xh;x++)
		{
			if (actorat[x][y] && !(actorat[x][y] & 0x8000))
				return false;
		}

//
// check for actors
//
	if (yl>0)
		yl--;
	if (yh<MAPSIZE-1)
		yh++;
	if (xl>0)
		xl--;
	if (xh<MAPSIZE-1)
		xh++;

	for (y=yl;y<=yh;y++)
		for (x=xl;x<=xh;x++)
		{
			if (actorat[x][y] & 0x8000) {
				check = &objlist[actorat[x][y] & ~0x8000];

				if (check->flags & FL_SHOOTABLE)
				{
					deltax = ob->x - check->x;
					if (deltax < -MINACTORDIST || deltax > MINACTORDIST)
						continue;
					deltay = ob->y - check->y;
					if (deltay < -MINACTORDIST || deltay > MINACTORDIST)
						continue;

					return false;
				}
			}
		}

	return true;
}

/*
===================
=
= ClipMove
=
===================
*/

void ClipMove(objtype *ob, long xmove, long ymove)
{
	long	basex,basey;

	basex = ob->x;
	basey = ob->y;

	ob->x = basex+xmove;
	ob->y = basey+ymove;
	if (TryMove(ob))
		return;

	if (noclip && ob->x > 2*TILEGLOBAL && ob->y > 2*TILEGLOBAL &&
	ob->x < (((long)(mapwidth-1))<<TILESHIFT)
	&& ob->y < (((long)(mapheight-1))<<TILESHIFT) )
		return;		// walk through walls

	if (!SD_SoundPlaying()){
		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (HITWALLSNDWL6);
		} else {
			
			SD_PlaySoundSOD (HITWALLSNDSOD);
		}
	}

	ob->x = basex+xmove;
	ob->y = basey;
	if (TryMove(ob))
		return;

	ob->x = basex;
	ob->y = basey+ymove;
	if (TryMove(ob))
		return;

	ob->x = basex;
	ob->y = basey;
}

//==========================================================================

/*
===================
=
= VictoryTile
=
===================
*/

void VictoryTile()
{
	if (w0 == true || w1 == true){
		SpawnBJVictory();
	}
	gamestate.victoryflag = true;
}

/*
===================
=
= Thrust
=
===================
*/

void Thrust(int angle, long speed)
{
	long xmove,ymove;
	unsigned offset;

	//
	// ZERO FUNNY COUNTER IF MOVED!
	//
	if (speed)
		funnyticount = 0;

	thrustspeed += speed;
//
// moving bounds speed
//
	if (speed >= MINDIST*2)
		speed = MINDIST*2-1;

	xmove = FixedByFrac(speed,costable[angle]);
	ymove = -FixedByFrac(speed,sintable[angle]);

	ClipMove(player,xmove,ymove);

	player->tilex = player->x >> TILESHIFT;		// scale to tile values
	player->tiley = player->y >> TILESHIFT;

	offset = farmapylookup[player->tiley]+player->tilex;
	player->areanumber = *(mapsegs[0] + offset) -AREATILE;

	if (*(mapsegs[1] + offset) == EXITTILE)
		VictoryTile();
}


/*
=============================================================================

								ACTIONS

=============================================================================
*/


/*
===============
=
= Cmd_Fire
=
===============
*/

void Cmd_Fire()
{
	buttonheld[bt_attack] = true;

	gamestate.weaponframe = 0;

	if (w0 == true || w1 == true){
		player->state = s_attackWL6;
	} else {
		player->state = s_attackSOD;
	}

	gamestate.attackframe = 0;
	gamestate.attackcount =
		attackinfo[gamestate.weapon][gamestate.attackframe].tics;
	gamestate.weaponframe =
		attackinfo[gamestate.weapon][gamestate.attackframe].frame;
}

//===========================================================================

/*
===============
=
= Cmd_Use
=
===============
*/

void Cmd_Use()
{
	int checkx, checky, doornum, dir;
	boolean elevatorok;

//
// find which cardinal direction the player is facing
//
	if (player->angle < ANGLES/8 || player->angle > 7*ANGLES/8)
	{
		checkx = player->tilex + 1;
		checky = player->tiley;
		dir = di_east;
		elevatorok = true;
	}
	else if (player->angle < 3*ANGLES/8)
	{
		checkx = player->tilex;
		checky = player->tiley-1;
		dir = di_north;
		elevatorok = false;
	}
	else if (player->angle < 5*ANGLES/8)
	{
		checkx = player->tilex - 1;
		checky = player->tiley;
		dir = di_west;
		elevatorok = true;
	}
	else
	{
		checkx = player->tilex;
		checky = player->tiley + 1;
		dir = di_south;
		elevatorok = false;
	}

	doornum = tilemap[checkx][checky];
	if (*(mapsegs[1]+farmapylookup[checky]+checkx) == PUSHABLETILE)
	{
	//
	// pushable wall
	//

		PushWall (checkx,checky,dir);
		return;
	}
	if (!buttonheld[bt_use] && doornum == ELEVATORTILE && elevatorok)
	{
	//
	// use elevator
	//
		buttonheld[bt_use] = true;

		tilemap[checkx][checky]++;		// flip switch
		if (*(mapsegs[0]+farmapylookup[player->tiley]+player->tilex) == ALTELEVATORTILE)
			playstate = ex_secretlevel;
		else
			playstate = ex_completed;
		if (w0 == true || w1 == true){
			SD_PlaySoundWL6(LEVELDONESNDWL6);
		} else {
			SD_PlaySoundSOD(LEVELDONESNDSOD);
		}
		SD_WaitSoundDone();
	}
	else if (!buttonheld[bt_use] && doornum & 0x80)
	{
		buttonheld[bt_use] = true;
		OperateDoor(doornum & ~0x80);
	}
	else
		if (w0 == true || w1 == true){
			SD_PlaySoundWL6(DONOTHINGSNDWL6);
		} else {
			SD_PlaySoundSOD(DONOTHINGSNDSOD);
		}

}

/*
=============================================================================

						   PLAYER CONTROL

=============================================================================
*/



/*
===============
=
= SpawnPlayer
=
===============
*/

void SpawnPlayer (int tilex, int tiley, int dir)
{
	player->obclass = playerobj;
	player->active = ac_yes;
	player->tilex = tilex;
	player->tiley = tiley;
	player->areanumber =
		*(mapsegs[0] + farmapylookup[player->tiley]+player->tilex);
	player->x = ((long)tilex<<TILESHIFT)+TILEGLOBAL/2;
	player->y = ((long)tiley<<TILESHIFT)+TILEGLOBAL/2;
	if (w0 == true || w1 == true){
		player->state = s_playerWL6;
	} else {
		player->state = s_playerSOD;
	}
	player->angle = (1-dir)*90;
	if (player->angle<0)
		player->angle += ANGLES;
	player->flags = FL_NEVERMARK;
	Thrust (0,0);				// set some variables

	InitAreas();
}


//===========================================================================

/*
===============
=
= T_KnifeAttack
=
= Update player hands, and try to do damage when the proper frame is reached
=
===============
*/

void KnifeAttack (objtype *ob)
{
	objtype *check,*closest;
	long	dist;

	if (w0 == true || w1 == true){
		SD_PlaySoundWL6 (ATKKNIFESNDWL6);
	} else {
		SD_PlaySoundSOD (ATKKNIFESNDSOD);
	}
// actually fire
	dist = 0x7fffffff;
	closest = NULL;
	for (check=ob->next ; check ; check=check->next)
		if ( (check->flags & FL_SHOOTABLE)
		&& (check->flags & FL_VISABLE)
		&& abs (check->viewx-centerx) < shootdelta
		)
		{
			if (check->transx < dist)
			{
				dist = check->transx;
				closest = check;
			}
		}

	if (!closest || dist> 0x18000l)
	{
	// missed

		return;
	}

// hit something
	DamageActor(closest, US_RndT() >> 4);
}



void GunAttack(objtype *ob)
{
	objtype *check, *closest, *oldclosest;
	int		damage;
	int		dx,dy,dist;
	long	viewdist;

	switch (gamestate.weapon)
	{
	case wp_pistol:
		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (ATKPISTOLSNDWL6);
		} else {
			SD_PlaySoundSOD (ATKPISTOLSNDSOD);
		}
		break;
	case wp_machinegun:
		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (ATKMACHINEGUNSNDWL6);
		} else {
			SD_PlaySoundSOD (ATKMACHINEGUNSNDSOD);
		}
		break;
	case wp_chaingun:
		if (w0 == true || w1 == true){
			SD_PlaySoundWL6 (ATKGATLINGSNDWL6);
		} else {
			SD_PlaySoundSOD (ATKGATLINGSNDSOD);
		}
		break;
	default:
		break;
	}

	madenoise = true;

//
// find potential targets
//
	viewdist = 0x7fffffffl;
	closest = NULL;

	while (1)
	{
		oldclosest = closest;

		for (check=ob->next ; check ; check=check->next)
			if ( (check->flags & FL_SHOOTABLE)
			&& (check->flags & FL_VISABLE)
			&& abs (check->viewx-centerx) < shootdelta
			)
			{
				if (check->transx < viewdist)
				{
					viewdist = check->transx;
					closest = check;
				}
			}

		if (closest == oldclosest)
			return;		// no more targets, all missed

	//
	// trace a line from player to enemey
	//
		if (CheckLine(closest))
			break;

	}

//
// hit something
//
	dx = abs(closest->tilex - player->tilex);
	dy = abs(closest->tiley - player->tiley);
	dist = dx>dy ? dx:dy;

	if (dist<2)
		damage = US_RndT() / 4;
	else if (dist<4)
		damage = US_RndT() / 6;
	else
	{
		if ( (US_RndT() / 12) < dist)		// missed
			return;
		damage = US_RndT() / 6;
	}

	DamageActor (closest,damage);
}

//===========================================================================

/*
===============
=
= VictorySpin
=
===============
*/

void VictorySpin()
{
	long desty;

	if (player->angle > 270)
	{
		player->angle -= tics * 3;
		if (player->angle < 270)
			player->angle = 270;
	}
	else if (player->angle < 270)
	{
		player->angle += tics * 3;
		if (player->angle > 270)
			player->angle = 270;
	}

	desty = (((long)player->tiley-5)<<TILESHIFT)-0x3000;

	if (player->y > desty)
	{
		player->y -= tics*4096;
		if (player->y < desty)
			player->y = desty;
	}
}


//===========================================================================

/*
===============
=
= T_Attack
=
===============
*/

void T_Attack(objtype *ob)
{
	const struct atkinf *cur;

	UpdateFace();

	if (gamestate.victoryflag)		// watching the BJ actor
	{
		VictorySpin();
		return;
	}

	if ( buttonstate[bt_use] && !buttonheld[bt_use] )
		buttonstate[bt_use] = false;

	if ( buttonstate[bt_attack] && !buttonheld[bt_attack])
		buttonstate[bt_attack] = false;

	ControlMovement(ob);
	if (gamestate.victoryflag)		// watching the BJ actor
		return;

	plux = player->x >> UNSIGNEDSHIFT;			// scale to fit in unsigned
	pluy = player->y >> UNSIGNEDSHIFT;
	player->tilex = player->x >> TILESHIFT;		// scale to tile values
	player->tiley = player->y >> TILESHIFT;

//
// change frame and fire
//
	gamestate.attackcount -= tics;
	while (gamestate.attackcount <= 0)
	{
		cur = &attackinfo[gamestate.weapon][gamestate.attackframe];
		switch (cur->attack)
		{
		case -1:
			if (w0 == true || w1 == true){
				ob->state = s_playerWL6;
			} else {
				ob->state = s_playerSOD;
			}
			if (!gamestate.ammo)
			{
				gamestate.weapon = wp_knife;
				DrawWeapon ();
			}
			else
			{
				if (gamestate.weapon != gamestate.chosenweapon)
				{
					gamestate.weapon = gamestate.chosenweapon;
					DrawWeapon ();
				}
			};
			gamestate.attackframe = gamestate.weaponframe = 0;
			return;

		case 4:
			if (!gamestate.ammo)
				break;
			if (buttonstate[bt_attack])
				gamestate.attackframe -= 2;
		case 1:
			if (!gamestate.ammo)
			{	// can only happen with chain gun
				gamestate.attackframe++;
				break;
			}
			GunAttack (ob);
			gamestate.ammo--;
			DrawAmmo ();
			break;

		case 2:
			KnifeAttack (ob);
			break;

		case 3:
			if (gamestate.ammo && buttonstate[bt_attack])
				gamestate.attackframe -= 2;
			break;
		}

		gamestate.attackcount += cur->tics;
		gamestate.attackframe++;
		gamestate.weaponframe =
			attackinfo[gamestate.weapon][gamestate.attackframe].frame;
	}

}

/*
===============
=
= T_Player
=
===============
*/

void T_Player(objtype *ob)
{
	if (gamestate.victoryflag)		// watching the BJ actor
	{
		VictorySpin();
		return;
	}

	UpdateFace();
	CheckWeaponChange();

	if (buttonstate[bt_use])
		Cmd_Use();

	if (buttonstate[bt_attack] && !buttonheld[bt_attack])
		Cmd_Fire();

	ControlMovement(ob);
	if (gamestate.victoryflag)		// watching the BJ actor
		return;

	plux = player->x >> UNSIGNEDSHIFT;			// scale to fit in unsigned
	pluy = player->y >> UNSIGNEDSHIFT;
	player->tilex = player->x >> TILESHIFT;		// scale to tile values
	player->tiley = player->y >> TILESHIFT;
}
