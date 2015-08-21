/**************************************************************************************************
  Filename:       bedservice.c
  Revised:        $Date: 2013-09-07 $
  Revision:       $Revision:  $

  Description:    Bed Sensor Service


  Copyright 2013 Soft Edge Technologies. All rights reserved.

  Based on code template:
  Copyright 2012 Texas Instruments Incorporated. All rights reserved.

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
  and/or Soft Edge Technologies www.set-ltd.com

**************************************************************************************************/

/*********************************************************************
 * INCLUDES
 */
#include "bcomdef.h"
#include "linkdb.h"
#include "gatt.h"
#include "gatt_uuid.h"
#include "gattservapp.h"

#include "bedservice.h"
#include "st_util.h"

/*********************************************************************
 * MACROS
 */

/*********************************************************************
 * CONSTANTS
 */

/*********************************************************************
 * TYPEDEFS
 */

/*********************************************************************
 * GLOBAL VARIABLES
 */

// Bed Sensor Service UUID
CONST uint8 bedServUUID[TI_UUID_SIZE] =
{
  TI_UUID(BED_SERV_UUID),
};

// Bed Sensor Characteristic value Data UUID
CONST uint8 bedDataUUID[TI_UUID_SIZE] =
{
  TI_UUID(BED_DATA_UUID),
};

// Bed Sensor Characteristic value Configuration UUID
//CONST uint8 bedCfgUUID[TI_UUID_SIZE] =
//{
//  TI_UUID(BED_CONF_UUID),
//};


/*********************************************************************
 * EXTERNAL VARIABLES
 */

/*********************************************************************
 * EXTERNAL FUNCTIONS
 */

/*********************************************************************
 * LOCAL VARIABLES
 */

static bedCBs_t *bed_AppCBs = NULL;

/*********************************************************************
 * Profile Attributes - variables
 */


// Bed Sensor Profile Service attribute
static CONST gattAttrType_t bedService = { TI_UUID_SIZE, bedServUUID };

// Bed Sensor Characteristic Properties
static uint8 bedDataProps = GATT_PROP_READ;

static uint8 bedData[BED_DATA_LEN] = {0};

// Bed Sensor Characteristic Configuration
static gattCharCfg_t bedDataConfig[GATT_MAX_NUM_CONN];

// Bed Sensor Characteristic User Description
static uint8 bedDataUserDesp[] = "Bed Data";

// Bed Sensor Characteristic Configuration Properties
static uint8 bedCfgProps = GATT_PROP_READ | GATT_PROP_WRITE;

// Bed Sensor Characteristic Configuration Value
static uint8 bedCfg = 0;

// Bed Sensor Characteristic Configuration User Description
static uint8 bedCfgUserDesp[] = "Bed Conf.";


/*********************************************************************
 * Profile Attributes - Table
 */

#if 0
static gattAttribute_t sensorBedAttrTbl[] =
{
  {
    { ATT_BT_UUID_SIZE, primaryServiceUUID }, /* type */
    GATT_PERMIT_READ,                         /* permissions */
    0,                                        /* handle */
    (uint8 *)&bedService                      /* pValue */
  },

  // Characteristic Declaration
  {
    { ATT_BT_UUID_SIZE, characterUUID },
    GATT_PERMIT_READ,
    0,
    &bedDataProps
  },

  // Characteristic Value "Data"
  {
    { TI_UUID_SIZE, bedDataUUID },
    GATT_PERMIT_READ,
    0,
    bedData
  },
  
  // Characteristic user desc
  {
    { ATT_BT_UUID_SIZE, charUserDescUUID },
    GATT_PERMIT_READ,
    0,
    bedDataUserDesp
  }
};
#endif

/*
  V1.11 Revert to V1 attributes
*/
static gattAttribute_t sensorBedAttrTbl[] =
{
  {
    { ATT_BT_UUID_SIZE, primaryServiceUUID }, /* type */
    GATT_PERMIT_READ,                         /* permissions */
    0,                                        /* handle */
    (uint8 *)&bedService                      /* pValue */
  },

  // Characteristic Value "Data"
  {
    { TI_UUID_SIZE, bedDataUUID },
    GATT_PERMIT_READ,
    0,
    bedData
  }
};

/*********************************************************************
 * LOCAL FUNCTIONS
 */
static uint8 bed_ReadAttrCB( uint16 connHandle, gattAttribute_t *pAttr,
                            uint8 *pValue, uint8 *pLen, uint16 offset, uint8 maxLen );
//static bStatus_t bed_WriteAttrCB( uint16 connHandle, gattAttribute_t *pAttr,
//                                 uint8 *pValue, uint8 len, uint16 offset );
static void bed_HandleConnStatusCB( uint16 connHandle, uint8 changeType );

/*********************************************************************
 * PROFILE CALLBACKS
 */
// Simple Profile Service Callbacks
CONST gattServiceCBs_t bedCBs =
{
  bed_ReadAttrCB,   // Read callback function pointer
  NULL,             //bed_WriteAttrCB, // Write callback function pointer
  NULL              // Authorization callback function pointer
};

/*********************************************************************
 * PUBLIC FUNCTIONS
 */

/*********************************************************************
 * @fn      Bed_AddService
 *
 * @brief   Initializes the Sensor Profile service by registering
 *          GATT attributes with the GATT server.
 *
 * @param   services - services to add. This is a bit map and can
 *                     contain more than one service.
 *
 * @return  Success or Failure
 */
bStatus_t Bed_AddService( uint32 services )
{
  uint8 status = SUCCESS;

  // Register with Link DB to receive link status change callback
  VOID linkDB_Register( bed_HandleConnStatusCB );

  if (services & BED_SERVICE )
  {

    // Register GATT attribute list and CBs with GATT Server App
    status = GATTServApp_RegisterService( sensorBedAttrTbl,
                                          GATT_NUM_ATTRS( sensorBedAttrTbl ),
                                          &bedCBs );
  }

  return ( status );
}


/*********************************************************************
 * @fn      Bed_RegisterAppCBs
 *
 * @brief   Registers the application callback function. Only call
 *          this function once.
 *
 * @param   callbacks - pointer to application callbacks.
 *
 * @return  SUCCESS or bleAlreadyInRequestedMode
 */
bStatus_t Bed_RegisterAppCBs( bedCBs_t *appCallbacks )
{
  if ( bed_AppCBs == NULL )
  {
    if ( appCallbacks != NULL )
    {
      bed_AppCBs = appCallbacks;
    }

    return ( SUCCESS );
  }

  return ( bleAlreadyInRequestedMode );
}

/*********************************************************************
 * @fn      Bed_SetParameter
 *
 * @brief   Set a Sensor Profile parameter.
 *
 * @param   param - Profile parameter ID
 * @param   len - length of data to right
 * @param   value - pointer to data to write.  This is dependent on
 *          the parameter ID and WILL be cast to the appropriate
 *          data type (example: data type of uint16 will be cast to
 *          uint16 pointer).
 *
 * @return  bStatus_t
 */
bStatus_t Bed_SetParameter( uint8 param, uint8 len, void *value )
{
  bStatus_t ret = SUCCESS;

  switch ( param )
  {
    case BED_DATA:
      if ( len == BED_DATA_LEN )
      {
        VOID osal_memcpy( bedData, value, BED_DATA_LEN );

        // See if Notification has been enabled
        GATTServApp_ProcessCharCfg( bedDataConfig, bedData, FALSE,
                                   sensorBedAttrTbl, GATT_NUM_ATTRS( sensorBedAttrTbl ),
                                   INVALID_TASK_ID );
      }
      else
      {
        ret = bleInvalidRange;
      }
      break;

    /*  
    case BED_CONF:
      if(len == sizeof ( uint8 ) )
      {
        bedCfg = *((uint8*)value);
      }
      else
      {
        ret = bleInvalidRange;
      }
      break;
    */

    default:
        ret = INVALIDPARAMETER;
        break;
  }

  return ( ret );
}

/*********************************************************************
 * @fn      Bed_GetParameter
 *
 * @brief   Get a Sensor Profile parameter.
 *
 * @param   param - Profile parameter ID
 * @param   value - pointer to data to put.  This is dependent on
 *          the parameter ID and WILL be cast to the appropriate
 *          data type (example: data type of uint16 will be cast to
 *          uint16 pointer).
 *
 * @return  bStatus_t
 */
bStatus_t Bed_GetParameter( uint8 param, void *value )
{
  bStatus_t ret = SUCCESS;

  switch ( param )
  {
    case BED_DATA:
        VOID osal_memcpy (value, bedData, BED_DATA_LEN );
        break;

    //case BED_CONF:
    //    *((uint8*)value) = bedCfg;
    //    break;

    default:
      ret = INVALIDPARAMETER;
      break;
  }

  return ( ret );
}

/*********************************************************************
 * @fn          bed_ReadAttrCB
 *
 * @brief       Read an attribute.
 *
 * @param       connHandle - connection message was received on
 * @param       pAttr - pointer to attribute
 * @param       pValue - pointer to data to be read
 * @param       pLen - length of data to be read
 * @param       offset - offset of the first octet to be read
 * @param       maxLen - maximum length of data to be read
 *
 * @return      Success or Failure
 */
static uint8 bed_ReadAttrCB( uint16 connHandle, gattAttribute_t *pAttr,
                            uint8 *pValue, uint8 *pLen, uint16 offset, uint8 maxLen )
{
  uint16 uuid;
  bStatus_t status = SUCCESS;

  // If attribute permissions require authorization to read, return error
  if ( gattPermitAuthorRead( pAttr->permissions ) )
  {
    // Insufficient authorization
    return ( ATT_ERR_INSUFFICIENT_AUTHOR );
  }

  // Make sure it's not a blob operation (no attributes in the profile are long)
  if ( offset > 0 )
  {
    return ( ATT_ERR_ATTR_NOT_LONG );
  }

  if (utilExtractUuid16(pAttr,&uuid) == FAILURE) {
    // Invalid handle
    *pLen = 0;
    return ATT_ERR_INVALID_HANDLE;
  }

  switch ( uuid )
  {
    // No need for "GATT_SERVICE_UUID" or "GATT_CLIENT_CHAR_CFG_UUID" cases;
    // gattserverapp handles those reads
    case BED_DATA_UUID:
      *pLen = BED_DATA_LEN;
      VOID osal_memcpy( pValue, pAttr->pValue, BED_DATA_LEN );
      break;

    //case BED_CONF_UUID:
    // *pLen = 1;
    //  pValue[0] = *pAttr->pValue;
    //  break;

    default:
      *pLen = 0;
      status = ATT_ERR_ATTR_NOT_FOUND;
      break;
  }

  return ( status );
}

/*********************************************************************
* @fn      bed_WriteAttrCB
*
* @brief   Validate attribute data prior to a write operation
*
* @param   connHandle - connection message was received on
* @param   pAttr - pointer to attribute
* @param   pValue - pointer to data to be written
* @param   len - length of data
* @param   offset - offset of the first octet to be written
* @param   complete - whether this is the last packet
* @param   oper - whether to validate and/or write attribute value
*
* @return  Success or Failure
*/

#if 0
static bStatus_t bed_WriteAttrCB( uint16 connHandle, gattAttribute_t *pAttr,
                                           uint8 *pValue, uint8 len, uint16 offset )
{
  uint16 uuid;
  bStatus_t status = SUCCESS;
  uint8 notifyApp = 0xFF;

  // If attribute permissions require authorization to write, return error
  if ( gattPermitAuthorWrite( pAttr->permissions ) )
  {
    // Insufficient authorization
    return ( ATT_ERR_INSUFFICIENT_AUTHOR );
  }

  if (utilExtractUuid16(pAttr,&uuid) == FAILURE) {
    // Invalid handle
    return ATT_ERR_INVALID_HANDLE;
  }

  switch ( uuid )
  {
    case BED_DATA_UUID:
      //Should not get here
      break;

    case BED_CONF_UUID:
      //Validate the value
      // Make sure it's not a blob oper
      if ( offset == 0 )
      {
        if ( len != 1 )
        {
          status = ATT_ERR_INVALID_VALUE_SIZE;
        }
      }
      else
      {
        status = ATT_ERR_ATTR_NOT_LONG;
      }

      //Write the value
      if ( status == SUCCESS )
      {
        uint8 *pCurValue = (uint8 *)pAttr->pValue;
        *pCurValue = pValue[0];

        if( pAttr->pValue == &bedCfg )
        {
          notifyApp = BED_CONF;
        }
      }
      break;

    case GATT_CLIENT_CHAR_CFG_UUID:
      status = GATTServApp_ProcessCCCWriteReq( connHandle, pAttr, pValue, len,
                                              offset, GATT_CLIENT_CFG_NOTIFY );
      break;

    default:
      // Should never get here!
      status = ATT_ERR_ATTR_NOT_FOUND;
      break;
  }

  // If a charactersitic value changed then callback function to notify application of change
  if ( (notifyApp != 0xFF ) && bed_AppCBs && bed_AppCBs->pfnBedChange )
  {
    bed_AppCBs->pfnBedChange( notifyApp );
  }

  return ( status );
}
#endif

/*********************************************************************
 * @fn          bed_HandleConnStatusCB
 *
 * @brief       Sensor Profile link status change handler function.
 *
 * @param       connHandle - connection handle
 * @param       changeType - type of change
 *
 * @return      none
 */
static void bed_HandleConnStatusCB( uint16 connHandle, uint8 changeType )
{
  // Make sure this is not loopback connection
  if ( connHandle != LOOPBACK_CONNHANDLE )
  {
    // Reset Client Char Config if connection has dropped
    if ( ( changeType == LINKDB_STATUS_UPDATE_REMOVED )      ||
         ( ( changeType == LINKDB_STATUS_UPDATE_STATEFLAGS ) &&
           ( !linkDB_Up( connHandle ) ) ) )
    {
      GATTServApp_InitCharCfg( connHandle, bedDataConfig );
    }
  }
}


/*********************************************************************
*********************************************************************/
