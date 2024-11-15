/* tslint:disable */
/* eslint-disable */
/**
 * Variable Density Yield Projection
 * API for the Variable Density Yield Projection service
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

import { EnumSeverity } from './enum-severity';
 /**
 * 
 *
 * @export
 * @interface MessagesInner
 */
export interface MessagesInner {

    /**
     * the message's unique identifier
     *
     * @type {string}
     * @memberof MessagesInner
     */
    id?: string;

    /**
     * the id of the layer to which the message applies. This value is null if this is a polygon level message and so does not apply to a specific layer
     *
     * @type {string}
     * @memberof MessagesInner
     */
    layerId?: string;

    /**
     * the id of the stand component to which the message applies. This value will be null if not known or applicable
     *
     * @type {string}
     * @memberof MessagesInner
     */
    standComponentId?: string;

    /**
     * the element of the ReturnCode enumeration returned from the operation that resulted in this message being generated
     *
     * @type {string}
     * @memberof MessagesInner
     */
    errorCode?: string;

    /**
     * @type {EnumSeverity}
     * @memberof MessagesInner
     */
    severity?: EnumSeverity;

    /**
     * the element of the MessageCode enumeration describing this message
     *
     * @type {string}
     * @memberof MessagesInner
     */
    messageCode?: string;

    /**
     * the message contents
     *
     * @type {string}
     * @memberof MessagesInner
     */
    message?: string;
}