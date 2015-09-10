/**************************************************************************************************
  Filename:       hal_bed.c
  Revised:        $Date: 2013-09-26 $
  Revision:       $Revision:  $

  Description:    Driver for the Bed Sensor.

  Copyright 2013 Soft Edge Technologies. All rights reserved.

  Based on code template:
  Copyright 2012-2013 Texas Instruments Incorporated. All rights reserved.

  IMPORTANT: Your use of this Software is limited to those specific rights
  granted under the terms of a software license agreement between the user
  who downloaded the software, his/her employer (which must be your employer)
  and Texas Instruments Incorporated (the "License").  You may not use this
  Software unless you agree to abide by the terms of the License. The License
  limits your use, and you acknowledge, that the Software may not be modified,
  copied or distributed unless embedded on a Texas Instruments microcontroller
  or used solely and exclusively in conjunction with a Texas Instruments radio
  frequency transceiver, which is integrated into your product.  Other than for
  the foregoing purpose, you may not use, reproduce, copy, prepare derivative
  works of, modify, distribute, perform, display or sell this Software and/or
  its documentation for any purpose.

  YOU FURTHER ACKNOWLEDGE AND AGREE THAT THE SOFTWARE AND DOCUMENTATION ARE
  PROVIDED “AS IS” WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED,
  INCLUDING WITHOUT LIMITATION, ANY WARRANTY OF MERCHANTABILITY, TITLE,
  NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT SHALL
  TEXAS INSTRUMENTS OR ITS LICENSORS BE LIABLE OR OBLIGATED UNDER CONTRACT,
  NEGLIGENCE, STRICT LIABILITY, CONTRIBUTION, BREACH OF WARRANTY, OR OTHER
  LEGAL EQUITABLE THEORY ANY DIRECT OR INDIRECT DAMAGES OR EXPENSES
  INCLUDING BUT NOT LIMITED TO ANY INCIDENTAL, SPECIAL, INDIRECT, PUNITIVE
  OR CONSEQUENTIAL DAMAGES, LOST PROFITS OR LOST DATA, COST OF PROCUREMENT
  OF SUBSTITUTE GOODS, TECHNOLOGY, SERVICES, OR ANY CLAIMS BY THIRD PARTIES
  (INCLUDING BUT NOT LIMITED TO ANY DEFENSE THEREOF), OR OTHER SIMILAR COSTS.

  Should you have any questions regarding your right to use this Software,
  contact Texas Instruments Incorporated at www.TI.com.
  and/or Soft Edge Technologies at www.set-ltd.com

**************************************************************************************************/

/* ------------------------------------------------------------------------------------------------
*                                          Includes
* ------------------------------------------------------------------------------------------------
*/

#include "hal_sensor.h"
#include "hal_bed_1.1.h"
#include "hal_board_cfg.h"

/* ------------------------------------------------------------------------------------------------
*                                           Constants
* ------------------------------------------------------------------------------------------------
*/

/* ------------------------------------------------------------------------------------------------
*                                           Typedefs
* ------------------------------------------------------------------------------------------------
*/

/* ------------------------------------------------------------------------------------------------
*                                           Macros
* ------------------------------------------------------------------------------------------------
*/

/* ------------------------------------------------------------------------------------------------
*                                           Local Functions
* ------------------------------------------------------------------------------------------------
*/


/* ------------------------------------------------------------------------------------------------
*                                           Local Variables
* ------------------------------------------------------------------------------------------------
*/

/**************************************************************************************************
* @fn          HalBedInit
*
* @brief       This function initializes the HAL Bed Sensor abstraction layer.
*
* @return      None.
*/
void HalBedInit(void)
{
  // ensure P1.1 is an input
  P1DIR &= ~BV(1);
  
  // make P1.0 an output as it doesn't have a pull-up
  //P1DIR |= BV(0);
  
  HAL_BED_SEL &= ~(HAL_BED_BIT);    /* Set pin function to GPIO */
  HAL_BED_DIR &= ~(HAL_BED_BIT);    /* Set pin direction to Input */
}


/**************************************************************************************************
* @fn          HalBedRead
*
* @brief       Read data from the bed pressure sensor - 1 byte
*
* @return      TRUE if valid data, FALSE if not
*/
bool HalBedRead(uint8 *pBuf )
{
  uint8 active = 0;
  
  // Enable pull-up
  HAL_BED_PULL &= ~HAL_BED_BIT;
  
  // Wait for measurement ready (appx. 3 ms)
  ST_HAL_DELAY(400);

  // Read the port input line
  uint8 port_temp = HAL_BED_PORT;
  if (!(port_temp & HAL_BED_BIT))    
  {
    active = 1;
  }
  *pBuf = active;
  
  // Disable pull-up
  HAL_BED_PULL |= HAL_BED_BIT;
  
  return TRUE;
}


/**************************************************************************************************
 * @fn          HalBedTest
 *
 * @brief       Run a sensor self-test
 *
 * @return      TRUE if passed, FALSE if failed
 */
bool HalBedTest(void)
{
  return TRUE;
}

/* ------------------------------------------------------------------------------------------------
*                                           Private functions
* -------------------------------------------------------------------------------------------------
*/

/*********************************************************************
*********************************************************************/
