

import kotlin.js.Date
import kotlin.js.Json
import kotlin.js.Promise


/**
 *  Scriptable
 *
 * @author IvanEOD ( 9/28/2023 at 11:01 AM EST )
 */

external fun importModule(path: String): Any
external fun logError(message: Any)
external fun logWarning(message: Any)
external fun log(message: Any)

external object args {
    var plainTexts: Array<String>
    var urls: Array<String>
    var fileURLs: Array<String>
    var images: Array<Image>
    var queryParameters: Map<String, String>
    var shortcutParameter: Any
    var widgetParameter: Any
    var notification: Notification
}

open external class Alert {
    open var title: String
    open var message: String
    open fun addAction(title: String)
    open fun addDestructiveAction(title: String)
    open fun addCancelAction(title: String)
    open fun addTextField(placeholder: String = definedExternally, text: String = definedExternally): TextField
    open fun addSecureTextField(placeholder: String = definedExternally, text: String = definedExternally): TextField
    open fun textFieldValue(index: Number): String
    open fun present(): Promise<Number>
    open fun presentAlert(): Promise<Number>
    open fun presentSheet(): Promise<Number>
}

open external class Calendar {
    open var identifier: String
    open var title: String
    open var isSubscribed: Boolean
    open var allowsContentModifications: Boolean
    open var color: Color
    open fun supportsAvailability(availability: String): Boolean
    open fun save()
    open fun remove()

    companion object {
        fun forReminders(): Promise<Array<Calendar>>
        fun forEvents(): Promise<Array<Calendar>>
        fun forRemindersByTitle(title: String): Promise<Calendar>
        fun forEventsByTitle(title: String): Promise<Calendar>
        fun createForReminders(title: String): Promise<Calendar>
        fun findOrCreateForReminders(title: String): Promise<Calendar>
        fun defaultForReminders(): Promise<Calendar>
        fun defaultForEvents(): Promise<Calendar>
        fun presentPicker(allowMultiple: Boolean = definedExternally): Promise<Array<Calendar>>
    }
}

open external class CalendarEvent {
    open var identifier: String
    open var title: String
    open var location: String
    open var notes: String
    open var startDate: Date
    open var endDate: Date
    open var isAllDay: Boolean
    open var attendees: Array<Attendees>
    open var availability: String
    open var timeZone: String
    open var calendar: Calendar
    open fun addRecurrenceRule(recurrenceRule: RecurrenceRule)
    open fun removeAllRecurrenceRules()
    open fun save()
    open fun remove()
    open fun presentEdit(): Promise<CalendarEvent>

    interface Attendees {
        var isCurrentUser: Boolean
        var name: String
        var status: String
        var type: String
        var role: String
    }

    companion object {
        fun presentCreate(): Promise<CalendarEvent>
        fun today(calendars: Array<Calendar> = definedExternally): Promise<Array<CalendarEvent>>
        fun tomorrow(calendars: Array<Calendar> = definedExternally): Promise<Array<CalendarEvent>>
        fun yesterday(calendars: Array<Calendar> = definedExternally): Promise<Array<CalendarEvent>>
        fun thisWeek(calendars: Array<Calendar> = definedExternally): Promise<Array<CalendarEvent>>
        fun nextWeek(calendars: Array<Calendar> = definedExternally): Promise<Array<CalendarEvent>>
        fun lastWeek(calendars: Array<Calendar> = definedExternally): Promise<Array<CalendarEvent>>
        fun between(startDate: Date, endDate: Date, calendars: Array<Calendar> = definedExternally): Promise<Array<CalendarEvent>>
    }
}

open external class CallbackURL(baseURL: String) {
    open fun addParameter(name: String, value: String)
    open fun open(): Promise<Any>
}

open external class Color(hex: String, alpha: Number = definedExternally) {
    open var hex: String
    open var red: Number
    open var green: Number
    open var blue: Number
    open var alpha: Number

    companion object {
        fun black(): Color
        fun darkGray(): Color
        fun lightGray(): Color
        fun white(): Color
        fun gray(): Color
        fun red(): Color
        fun green(): Color
        fun blue(): Color
        fun cyan(): Color
        fun yellow(): Color
        fun magenta(): Color
        fun orange(): Color
        fun purple(): Color
        fun brown(): Color
        fun clear(): Color
        fun dynamic(lightColor: Color, darkColor: Color): Color
    }
}

external object config {
    var runsInApp: Boolean
    var runsInActionExtension: Boolean
    var runsWithSiri: Boolean
    var runsInWidget: Boolean
    var runsInAccessoryWidget: Boolean
    var runsInNotification: Boolean
    var runsFromHomeScreen: Boolean
    var widgetFamily: String
}

external object console {
    fun log(message: Any)
    fun warn(message: Any)
    fun error(message: Any)
}

open external class Contact {
    open var identifier: String
    open var namePrefix: String
    open var givenName: String
    open var middleName: String
    open var familyName: String
    open var nickname: String
    open var birthday: Date
    open var image: Image
    open var emailAddresses: Array<EmailAddresses>
    open var phoneNumbers: Array<PhoneNumbers>
    open var postalAddresses: Array<PostalAddresses>
    open var socialProfiles: Array<SocialProfiles>
    open var note: String
    open var urlAddresses: Array<Map<String, String>>
    open var dates: Array<Json>
    open var organizationName: String
    open var departmentName: String
    open var jobTitle: String
    open var isNamePrefixAvailable: Boolean
    open var isGiveNameAvailable: Boolean
    open var isMiddleNameAvailable: Boolean
    open var isFamilyNameAvailable: Boolean
    open var isNicknameAvailable: Boolean
    open var isBirthdayAvailable: Boolean
    open var isEmailAddressesAvailable: Boolean
    open var isPhoneNumbersAvailable: Boolean
    open var isPostalAddressesAvailable: Boolean
    open var isSocialProfilesAvailable: Boolean
    open var isImageAvailable: Boolean
    open var isNoteAvailable: Boolean
    open var isURLAddressesAvailable: Boolean
    open var isOrganizationNameAvailable: Boolean
    open var isDepartmentNameAvailable: Boolean
    open var isJobTitleAvailable: Boolean
    open var isDatesAvailable: Boolean

    interface EmailAddresses {
        var identifier: String?
            get() = definedExternally
            set(value) = definedExternally
        var label: String?
            get() = definedExternally
            set(value) = definedExternally
        var localizedLabel: String?
            get() = definedExternally
            set(value) = definedExternally
        var value: String
    }

    interface PhoneNumbers {
        var identifier: String?
            get() = definedExternally
            set(value) = definedExternally
        var label: String?
            get() = definedExternally
            set(value) = definedExternally
        var localizedLabel: String?
            get() = definedExternally
            set(value) = definedExternally
        var value: String
    }

    interface PostalAddresses {
        var identifier: String?
            get() = definedExternally
            set(value) = definedExternally
        var label: String
        var localizedLabel: String
        var street: String
        var city: String
        var state: String
        var postalCode: String
        var country: String
    }

    interface SocialProfiles {
        var identifier: String?
            get() = definedExternally
            set(value) = definedExternally
        var label: String
        var localizedLabel: String
        var service: String
        var url: String
        var userIdentifier: String
        var username: String
    }

    companion object {
        fun all(containers: Array<ContactsContainer>): Promise<Array<Contact>>
        fun inGroups(groups: Array<ContactsGroup>): Promise<Array<Contact>>
        fun add(contact: Contact, containerIdentifier: String = definedExternally)
        fun update(contact: Contact)
        fun delete(contact: Contact)
        fun persistChanges(): Promise<Unit>
    }
}

open external class ContactsContainer {
    open var identifier: String
    open var name: String

    companion object {
        fun default(): Promise<ContactsContainer>
        fun all(): Promise<Array<ContactsContainer>>
        fun withIdentifier(identifier: String): Promise<ContactsContainer>
    }
}

open external class ContactsGroup {
    open var identifier: String
    open var name: String
    open fun addMember(contact: Contact)
    open fun removeMember(contact: Contact)

    companion object {
        fun all(containers: Array<ContactsContainer>): Promise<Array<ContactsGroup>>
        fun add(group: ContactsGroup, containerIdentifier: String = definedExternally)
        fun update(group: ContactsGroup)
        fun delete(group: ContactsGroup)
    }
}

open external class Data {
    open fun toRawString(): String
    open fun toBase64String(): String
    open fun getBytes(): Array<Number>

    companion object {
        fun fromString(string: String): Data
        fun fromFile(filePath: String): Data
        fun fromBase64String(base64String: String): Data
        fun fromJPEG(image: Image): Data
        fun fromPNG(image: Image): Data
    }
}

open external class DateFormatter {
    open var dateFormat: String
    open var locale: String
    open fun string(date: Date): String
    open fun date(str: String): Date
    open fun useNoDateStyle()
    open fun useShortDateStyle()
    open fun useMediumDateStyle()
    open fun useLongDateStyle()
    open fun useFullDateStyle()
    open fun useNoTimeStyle()
    open fun useShortTimeStyle()
    open fun useMediumTimeStyle()
    open fun useLongTimeStyle()
    open fun useFullTimeStyle()
}

open external class DatePicker {
    open var minimumDate: Date
    open var maximumDate: Date
    open var countdownDuration: Number
    open var minuteInterval: Number
    open var initialDate: Date
    open fun pickTime(): Promise<Date>
    open fun pickDate(): Promise<Date>
    open fun pickDateAndTime(): Promise<Date>
    open fun pickCountdownDuration(): Promise<Number>
}

external object Device {
    fun name(): String
    fun systemName(): String
    fun systemVersion(): String
    fun model(): String
    fun isPhone(): Boolean
    fun isPad(): Boolean
    fun screenSize(): Size
    fun screenResolution(): Size
    fun screenScale(): Number
    fun screenBrightness(): Number
    fun isInPortrait(): Boolean
    fun isInPortraitUpsideDown(): Boolean
    fun isInLandscapeLeft(): Boolean
    fun isInLandscapeRight(): Boolean
    fun isFaceUp(): Boolean
    fun isFaceDown(): Boolean
    fun batteryLevel(): Number
    fun isDischarging(): Boolean
    fun isCharging(): Boolean
    fun isFullyCharged(): Boolean
    fun preferredLanguages(): Array<String>
    fun locale(): String
    fun language(): String
    fun isUsingDarkAppearance(): Boolean
    fun volume(): Number
    fun setScreenBrightness(percentage: Number)
}

external object Dictation {
    fun start(locale: String = definedExternally): Promise<String>
}

external object DocumentPicker {
    fun open(types: Array<String> = definedExternally): Promise<Array<String>>
    fun openFile(): Promise<String>
    fun openFolder(): Promise<String>
    fun export(path: String): Promise<Array<String>>
    fun exportString(content: String, name: String = definedExternally): Promise<Array<String>>
    fun exportImage(image: Image, name: String = definedExternally): Promise<Array<String>>
    fun exportData(data: Data, name: String = definedExternally): Promise<Array<String>>
}

open external class DrawContext {
    open var size: Size
    open var respectScreenScale: Boolean
    open var opaque: Boolean
    open fun getImage(): Image
    open fun drawImageInRect(image: Image, rect: Rect)
    open fun drawImageAtPoint(image: Image, point: Point)
    open fun setFillColor(color: Color)
    open fun setStrokeColor(color: Color)
    open fun setLineWidth(width: Number)
    open fun fill(rect: Rect)
    open fun fillRect(rect: Rect)
    open fun fillEllipse(rect: Rect)
    open fun stroke(rect: Rect)
    open fun strokeRect(rect: Rect)
    open fun strokeEllipse(rect: Rect)
    open fun addPath(path: Path)
    open fun strokePath()
    open fun fillPath()
    open fun drawText(text: String, pos: Point)
    open fun drawTextInRect(text: String, rect: Rect)
    open fun setFont(font: Font)
    open fun setTextColor(color: Color)
    open fun setTextAlignedLeft()
    open fun setTextAlignedCenter()
    open fun setTextAlignedRight()
}

open external class FileManager {
    open fun read(filePath: String): Data
    open fun readString(filePath: String): String
    open fun readImage(filePath: String): Image
    open fun write(filePath: String, content: Data)
    open fun writeString(filePath: String, content: String)
    open fun writeImage(filePath: String, image: Image)
    open fun remove(filePath: String)
    open fun move(sourceFilePath: String, destinationFilePath: String)
    open fun copy(sourceFilePath: String, destinationFilePath: String)
    open fun fileExists(filePath: String): Boolean
    open fun isDirectory(path: String): Boolean
    open fun createDirectory(path: String, intermediateDirectories: Boolean = definedExternally)
    open fun temporaryDirectory(): String
    open fun cacheDirectory(): String
    open fun documentsDirectory(): String
    open fun libraryDirectory(): String
    open fun joinPath(lhsPath: String, rhsPath: String): String
    open fun allTags(filePath: String): Array<String>
    open fun addTag(filePath: String, tag: String)
    open fun removeTag(filePath: String, tag: String)
    open fun readExtendedAttribute(filePath: String, name: String): String
    open fun writeExtendedAttribute(filePath: String, value: String, name: String)
    open fun removeExtendedAttribute(filePath: String, name: String)
    open fun allExtendedAttributes(filePath: String): Array<String>
    open fun getUTI(filePath: String): String
    open fun listContents(directoryPath: String): Array<String>
    open fun fileName(filePath: String, includeFileExtension: Boolean = definedExternally): String
    open fun fileExtension(filePath: String): String
    open fun bookmarkedPath(name: String): String
    open fun bookmarkExists(name: String): Boolean
    open fun downloadFileFromiCloud(filePath: String): Promise<Unit>
    open fun isFileStoredIniCloud(filePath: String): Boolean
    open fun isFileDownloaded(filePath: String): Boolean
    open fun creationDate(filePath: String): Date
    open fun modificationDate(filePath: String): Date
    open fun fileSize(filePath: String): Number
    open fun allFileBookmarks(): Array<AllFileBookmarks>

    interface AllFileBookmarks {
        var name: String
        var source: String
    }

    companion object {
        fun local(): FileManager
        fun iCloud(): FileManager
    }
}

open external class Font(name: String, size: Number) {
    companion object {
        fun largeTitle(): Font
        fun title1(): Font
        fun title2(): Font
        fun title3(): Font
        fun headline(): Font
        fun subheadline(): Font
        fun body(): Font
        fun callout(): Font
        fun footnote(): Font
        fun caption1(): Font
        fun caption2(): Font
        fun systemFont(size: Number): Font
        fun ultraLightSystemFont(size: Number): Font
        fun thinSystemFont(size: Number): Font
        fun lightSystemFont(size: Number): Font
        fun regularSystemFont(size: Number): Font
        fun mediumSystemFont(size: Number): Font
        fun semiboldSystemFont(size: Number): Font
        fun boldSystemFont(size: Number): Font
        fun heavySystemFont(size: Number): Font
        fun blackSystemFont(size: Number): Font
        fun italicSystemFont(size: Number): Font
        fun ultraLightMonospacedSystemFont(size: Number): Font
        fun thinMonospacedSystemFont(size: Number): Font
        fun lightMonospacedSystemFont(size: Number): Font
        fun regularMonospacedSystemFont(size: Number): Font
        fun mediumMonospacedSystemFont(size: Number): Font
        fun semiboldMonospacedSystemFont(size: Number): Font
        fun boldMonospacedSystemFont(size: Number): Font
        fun heavyMonospacedSystemFont(size: Number): Font
        fun blackMonospacedSystemFont(size: Number): Font
        fun ultraLightRoundedSystemFont(size: Number): Font
        fun thinRoundedSystemFont(size: Number): Font
        fun lightRoundedSystemFont(size: Number): Font
        fun regularRoundedSystemFont(size: Number): Font
        fun mediumRoundedSystemFont(size: Number): Font
        fun semiboldRoundedSystemFont(size: Number): Font
        fun boldRoundedSystemFont(size: Number): Font
        fun heavyRoundedSystemFont(size: Number): Font
        fun blackRoundedSystemFont(size: Number): Font
    }
}

open external class Image {
    open var size: Size

    companion object {
        fun fromFile(filePath: String): Image
        fun fromData(data: Data): Image
    }
}

external object Keychain {
    fun contains(key: String): Boolean
    fun set(key: String, value: String)
    fun get(key: String): String
    fun remove(key: String)
}

open external class LinearGradient {
    open var colors: Array<Color>
    open var locations: Array<Number>
    open var startPoint: Point
    open var endPoint: Point
}

open external class ListWidget {
    open var backgroundColor: Color
    open var backgroundImage: Image
    open var backgroundGradient: LinearGradient
    open var addAccessoryWidgetBackground: Boolean
    open var spacing: Number
    open var url: String
    open var refreshAfterDate: Date
    open fun addText(text: String): WidgetText
    open fun addDate(date: Date): WidgetDate
    open fun addImage(image: Image): WidgetImage
    open fun addSpacer(length: Number = definedExternally): WidgetSpacer
    open fun addStack(): WidgetStack
    open fun setPadding(top: Number, leading: Number, bottom: Number, trailing: Number)
    open fun useDefaultPadding()
    open fun presentSmall(): Promise<Unit>
    open fun presentMedium(): Promise<Unit>
    open fun presentLarge(): Promise<Unit>
    open fun presentExtraLarge(): Promise<Unit>
    open fun presentAccessoryInline(): Promise<Unit>
    open fun presentAccessoryCircular(): Promise<Unit>
    open fun presentAccessoryRectangular(): Promise<Unit>
}

open external class Mail {
    open var toRecipients: Array<String>
    open var ccRecipients: Array<String>
    open var bccRecipients: Array<String>
    open var subject: String
    open var body: String
    open var isBodyHTML: Boolean
    open var preferredSendingEmailAddress: String
    open fun send(): Promise<Unit>
    open fun addImageAttachment(image: Image)
    open fun addFileAttachment(filePath: String)
    open fun addDataAttachment(data: Data, mimeType: String, filename: String)
}

open external class Message {
    open var recipients: Array<String>
    open var body: String
    open fun send(): Promise<Unit>
    open fun addImageAttachment(image: Image)
    open fun addFileAttachment(filePath: String)
    open fun addDataAttachment(data: Data, uti: String, filename: String)
}

external object module {
    var filename: String
    var exports: Any
}

open external class Notification {
    open var identifier: String
    open var title: String
    open var subtitle: String
    open var body: String
    open var preferredContentHeight: Number
    open var badge: Number
    open var threadIdentifier: String
    open var userInfo: Json
    open var sound: String
    open var openURL: String
    open var deliveryDate: Date
    open var nextTriggerDate: Date
    open var scriptName: String
    open var actions: Any
    open fun schedule(): Promise<Unit>
    open fun remove(): Promise<Unit>
    open fun setTriggerDate(date: Date)
    open fun setDailyTrigger(hour: Number, minute: Number, repeats: Boolean = definedExternally)
    open fun setWeeklyTrigger(weekday: Number, hour: Number, minute: Number, repeats: Boolean = definedExternally)
    open fun addAction(title: String, url: String, destructive: Boolean = definedExternally)

    interface Actions {
        var title: String
        var url: String
    }

    companion object {
        fun allPending(): Promise<Array<Notification>>
        fun allDelivered(): Promise<Array<Notification>>
        fun removeAllPending(): Promise<Unit>
        fun removeAllDelivered(): Promise<Unit>
        fun removePending(identifiers: Array<String>): Promise<Unit>
        fun removeDelivered(identifiers: Array<String>): Promise<Unit>
        fun resetCurrent()
    }
}

external object Pasteboard {
    fun copy(string: String)
    fun paste(): String
    fun copyString(string: String)
    fun pasteString(): String
    fun copyImage(image: Image)
    fun pasteImage(): Image
}

open external class Path {
    open fun move(point: Point)
    open fun addLine(point: Point)
    open fun addRect(rect: Rect)
    open fun addEllipse(rect: Rect)
    open fun addRoundedRect(rect: Rect, cornerWidth: Number, cornerHeight: Number)
    open fun addCurve(point: Point, control1: Point, control2: Point)
    open fun addQuadCurve(point: Point, control: Point)
    open fun addLines(points: Array<Point>)
    open fun addRects(rects: Array<Rect>)
    open fun closeSubpath()
}

external object Photos {
    fun fromLibrary(): Promise<Image>
    fun fromCamera(): Promise<Image>
    fun latestPhoto(): Promise<Image>
    fun latestPhotos(count: Number): Promise<Array<Image>>
    fun latestScreenshot(): Promise<Image>
    fun latestScreenshots(count: Number): Promise<Array<Image>>
    fun removeLatestPhoto()
    fun removeLatestPhotos(count: Number)
    fun removeLatestScreenshot()
    fun removeLatestScreenshots(count: Number)
    fun save(image: Image)
}

open external class Point(x: Number, y: Number) {
    open var x: Number
    open var y: Number
}

external object QuickLook {
    fun present(item: Any, fullscreen: Boolean = definedExternally): Promise<Unit>
}

open external class Rect(x: Number, y: Number, width: Number, height: Number) {
    open var minX: Number
    open var minY: Number
    open var maxX: Number
    open var maxY: Number
    open var x: Number
    open var y: Number
    open var width: Number
    open var height: Number
    open var origin: Point
    open var size: Size
}

open external class RecurrenceRule {
    companion object {
        fun daily(interval: Number): RecurrenceRule
        fun dailyEndDate(interval: Number, endDate: Date): RecurrenceRule
        fun dailyOccurrenceCount(interval: Number, occurrenceCount: Number): RecurrenceRule
        fun weekly(interval: Number): RecurrenceRule
        fun weeklyEndDate(interval: Number, endDate: Date): RecurrenceRule
        fun weeklyOccurrenceCount(interval: Number, occurrenceCount: Number): RecurrenceRule
        fun monthly(interval: Number): RecurrenceRule
        fun monthlyEndDate(interval: Number, endDate: Date): RecurrenceRule
        fun monthlyOccurrenceCount(interval: Number, occurrenceCount: Number): RecurrenceRule
        fun yearly(interval: Number): RecurrenceRule
        fun yearlyEndDate(interval: Number, endDate: Date): RecurrenceRule
        fun yearlyOccurrenceCount(interval: Number, occurrenceCount: Number): RecurrenceRule
        fun complexWeekly(interval: Number, daysOfTheWeek: Array<Number>, setPositions: Array<Number>): RecurrenceRule
        fun complexWeeklyEndDate(interval: Number, daysOfTheWeek: Array<Number>, setPositions: Array<Number>, endDate: Date): RecurrenceRule
        fun complexWeeklyOccurrenceCount(interval: Number, daysOfTheWeek: Array<Number>, setPositions: Array<Number>, occurrenceCount: Number): RecurrenceRule
        fun complexMonthly(interval: Number, daysOfTheWeek: Array<Number>, daysOfTheMonth: Array<Number>, setPositions: Array<Number>): RecurrenceRule
        fun complexMonthlyEndDate(interval: Number, daysOfTheWeek: Array<Number>, daysOfTheMonth: Array<Number>, setPositions: Array<Number>, endDate: Date): RecurrenceRule
        fun complexMonthlyOccurrenceCount(interval: Number, daysOfTheWeek: Array<Number>, daysOfTheMonth: Array<Number>, setPositions: Array<Number>, occurrenceCount: Number): RecurrenceRule
        fun complexYearly(interval: Number, daysOfTheWeek: Array<Number>, monthsOfTheYear: Array<Number>, weeksOfTheYear: Array<Number>, daysOfTheYear: Array<Number>, setPositions: Array<Number>): RecurrenceRule
        fun complexYearlyEndDate(interval: Number, daysOfTheWeek: Array<Number>, monthsOfTheYear: Array<Number>, weeksOfTheYear: Array<Number>, daysOfTheYear: Array<Number>, setPositions: Array<Number>, endDate: Date): RecurrenceRule
        fun complexYearlyOccurrenceCount(interval: Number, daysOfTheWeek: Array<Number>, monthsOfTheYear: Array<Number>, weeksOfTheYear: Array<Number>, daysOfTheYear: Array<Number>, setPositions: Array<Number>, occurrenceCount: Number): RecurrenceRule
    }
}

open external class RelativeDateTimeFormatter {
    open var locale: String
    open fun string(date: Date, referenceDate: Date): String
    open fun useNamedDateTimeStyle()
    open fun useNumericDateTimeStyle()
}

open external class Reminder {
    open var identifier: String
    open var title: String
    open var notes: String
    open var isCompleted: Boolean
    open var isOverdue: Boolean
    open var priority: Number
    open var dueDate: Date?
    open var dueDateIncludesTime: Boolean
    open var completionDate: Date?
    open var creationDate: Date?
    open var calendar: Calendar
    open fun addRecurrenceRule(recurrenceRule: RecurrenceRule)
    open fun removeAllRecurrenceRules()
    open fun save()
    open fun remove()

    companion object {
        fun scheduled(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun all(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun allCompleted(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun allIncomplete(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun allDueToday(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun completedDueToday(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun incompleteDueToday(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun allDueTomorrow(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun completedDueTomorrow(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun incompleteDueTomorrow(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun allDueYesterday(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun completedDueYesterday(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun incompleteDueYesterday(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun allDueThisWeek(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun completedDueThisWeek(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun incompleteDueThisWeek(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun allDueNextWeek(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun completedDueNextWeek(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun incompleteDueNextWeek(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun allDueLastWeek(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun completedDueLastWeek(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun incompleteDueLastWeek(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun completedToday(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun completedThisWeek(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun completedLastWeek(calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun allDueBetween(startDate: Date, endDate: Date, calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun completedDueBetween(startDate: Date, endDate: Date, calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun incompleteDueBetween(startDate: Date, endDate: Date, calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
        fun completedBetween(startDate: Date, endDate: Date, calendars: Array<Calendar> = definedExternally): Promise<Array<Reminder>>
    }
}

open external class Request(url: String) {
    open var url: String
    open var method: String
    open var headers: Map<String, String>
    open var body: Any
    open var timeoutInterval: Number
    open var response: Json
    open var allowInsecureRequest: Boolean
    open fun load(): Promise<Data>
    open fun loadString(): Promise<String>
    open fun loadJSON(): Promise<Any>
    open fun loadImage(): Promise<Image>
    open fun addParameterToMultipart(name: String, value: String)
    open fun addFileDataToMultipart(data: Data, mimeType: String, name: String, filename: String)
    open fun addFileToMultipart(filePath: String, name: String, filename: String = definedExternally)
    open fun addImageToMultipart(image: Image, name: String, filename: String = definedExternally)
    open var onRedirect: (arg0: Request) -> Request
}

open external class SFSymbol {
    open var image: Image
    open fun applyFont(font: Font)
    open fun applyUltraLightWeight()
    open fun applyThinWeight()
    open fun applyLightWeight()
    open fun applyRegularWeight()
    open fun applyMediumWeight()
    open fun applySemiboldWeight()
    open fun applyBoldWeight()
    open fun applyHeavyWeight()
    open fun applyBlackWeight()

    companion object {
        fun named(symbolName: String): SFSymbol
    }
}

external object Safari {
    fun openInApp(url: String, fullscreen: Boolean = definedExternally): Promise<Unit>
    fun open(url: String)
}

external object Script {
    fun name(): String
    fun complete()
    fun setShortcutOutput(value: Any)
    fun setWidget(widget: Any)
}

external object ShareSheet {
    fun present(activityItems: Array<Any>): Promise<ShareSheetResult>
}

external interface ShareSheetResult {
    var completed: Boolean
    @JsName("activity_type")
    var activityType: String
}

open external class Size(width: Number, height: Number) {
    open var width: Number
    open var height: Number
}

external object Speech {
    fun speak(text: String)
}

open external class TextField {
    open var text: String
    open var placeholder: String
    open var isSecure: Boolean
    open var textColor: Color
    open var font: Font
    open fun setDefaultKeyboard()
    open fun setNumberPadKeyboard()
    open fun setDecimalPadKeyboard()
    open fun setNumbersAndPunctuationKeyboard()
    open fun setPhonePadKeyboard()
    open fun setWebSearchKeyboard()
    open fun setEmailAddressKeyboard()
    open fun setURLKeyboard()
    open fun setTwitterKeyboard()
    open fun leftAlignText()
    open fun centerAlignText()
    open fun rightAlignText()
}

open external class Timer {
    open var timeInterval: Number
    open var repeats: Boolean
    open fun schedule(callback: () -> Unit)
    open fun invalidate()

    companion object {
        fun schedule(timeInterval: Number, repeats: Boolean, callback: () -> Unit): Timer
    }
}

open external class UITable {
    open var showSeparators: Boolean
    open fun addRow(row: UITableRow)
    open fun removeRow(row: UITableRow)
    open fun removeAllRows()
    open fun reload()
    open fun present(fullscreen: Boolean = definedExternally): Promise<Unit>
}

open external class UITableCell {
    open var widthWeight: Number
    open var dismissOnTap: Boolean
    open var titleColor: Color
    open var subtitleColor: Color
    open var titleFont: Font
    open var subtitleFont: Font
    open fun leftAligned()
    open fun centerAligned()
    open fun rightAligned()
    open var onTap: () -> Unit

    companion object {
        fun text(title: String = definedExternally, subtitle: String = definedExternally): UITableCell
        fun image(image: Image): UITableCell
        fun imageAtURL(url: String): UITableCell
        fun button(title: String): UITableCell
    }
}

open external class UITableRow {
    open var cellSpacing: Number
    open var height: Number
    open var isHeader: Boolean
    open var dismissOnSelect: Boolean
    open var backgroundColor: Color
    open fun addCell(cell: UITableCell)
    open fun addText(title: String = definedExternally, subtitle: String = definedExternally): UITableCell
    open fun addImage(image: Image): UITableCell
    open fun addImageAtURL(url: String): UITableCell
    open fun addButton(title: String): UITableCell
    open var onSelect: () -> Unit
}

external object URLScheme {
    fun forOpeningScript(): String
    fun forOpeningScriptSettings(): String
    fun forRunningScript(): String
}

external object UUID {
    fun string(): String
}

open external class WebView {
    open fun loadURL(url: String): Promise<Unit>
    open fun loadRequest(request: Request): Promise<Unit>
    open fun loadHTML(html: String, baseURL: String = definedExternally): Promise<Unit>
    open fun loadFile(fileURL: String): Promise<Unit>
    open fun evaluateJavaScript(javaScript: String, useCallback: Boolean = definedExternally): Promise<Any>
    open fun getHTML(): Promise<Any>
    open fun present(fullscreen: Boolean = definedExternally): Promise<Unit>
    open fun waitForLoad(): Promise<Any>
    open var shouldAllowRequest: (arg0: Request) -> Boolean

    companion object {
        fun loadHTML(html: String, baseURL: String = definedExternally, preferredSize: Size = definedExternally, fullscreen: Boolean = definedExternally): Promise<Unit>
        fun loadFile(fileURL: String, preferredSize: Size = definedExternally, fullscreen: Boolean = definedExternally): Promise<Unit>
        fun loadURL(url: String, preferredSize: Size = definedExternally, fullscreen: Boolean = definedExternally): Promise<Unit>
    }
}

open external class WidgetDate {
    open var date: Date
    open var textColor: Color
    open var font: Font
    open var textOpacity: Number
    open var lineLimit: Number
    open var minimumScaleFactor: Number
    open var shadowColor: Color
    open var shadowRadius: Number
    open var shadowOffset: Point
    open var url: String
    open fun leftAlignText()
    open fun centerAlignText()
    open fun rightAlignText()
    open fun applyTimeStyle()
    open fun applyDateStyle()
    open fun applyRelativeStyle()
    open fun applyOffsetStyle()
    open fun applyTimerStyle()
}

open external class WidgetImage {
    open var image: Image
    open var resizable: Boolean
    open var imageSize: Size
    open var imageOpacity: Number
    open var cornerRadius: Number
    open var borderWidth: Number
    open var borderColor: Color
    open var containerRelativeShape: Boolean
    open var tintColor: Color
    open var url: String
    open fun leftAlignImage()
    open fun centerAlignImage()
    open fun rightAlignImage()
    open fun applyFittingContentMode()
    open fun applyFillingContentMode()
}

open external class WidgetSpacer {
    open var length: Number
}

open external class WidgetStack {
    open var backgroundColor: Color
    open var backgroundImage: Image
    open var backgroundGradient: LinearGradient
    open var spacing: Number
    open var size: Size
    open var cornerRadius: Number
    open var borderWidth: Number
    open var borderColor: Color
    open var url: String
    open fun addText(text: String): WidgetText
    open fun addDate(date: Date): WidgetDate
    open fun addImage(image: Image): WidgetImage
    open fun addSpacer(length: Number = definedExternally): WidgetSpacer
    open fun addStack(): WidgetStack
    open fun setPadding(top: Number, leading: Number, bottom: Number, trailing: Number)
    open fun useDefaultPadding()
    open fun topAlignContent()
    open fun centerAlignContent()
    open fun bottomAlignContent()
    open fun layoutHorizontally()
    open fun layoutVertically()
}

open external class WidgetText {
    open var text: String
    open var textColor: Color
    open var font: Font
    open var textOpacity: Number
    open var lineLimit: Number
    open var minimumScaleFactor: Number
    open var shadowColor: Color
    open var shadowRadius: Number
    open var shadowOffset: Point
    open var url: String
    open fun leftAlignText()
    open fun centerAlignText()
    open fun rightAlignText()
}

open external class XMLParser(string: String) {
    open var string: String
    open fun parse(): Boolean
    open var didStartDocument: () -> Unit
    open var didEndDocument: () -> Unit
    open var didStartElement: (arg0: String, arg1: Map<String, String>) -> Unit
    open var didEndElement: (name: String) -> Unit
    open var foundCharacters: (str: String) -> Unit
    open var parseErrorOccurred: (error: String) -> Unit
}


external interface CurrentLocation {
    var verticalAccuracy: Number
    var horizontalAccuracy: Number
    var altitude: Number
    var latitude: Number
    var longitude: Number
}

external interface GeocodePostalAddress {
    var country: String
    var postalCode: String
    var subAdministrativeArea: String
    var subLocality: String
    var state: String
    var street: String
    var city: String
    var isoCountryCode: String
}

external interface GeocodeLocation {
    var altitude: Number
    var longitude: Number
    var latitude: Number
}

external interface GeocodeSummary {
    var subAdministrativeArea: String?
    var postalAddress: GeocodePostalAddress
    var isoCountryCode: String?
    var timeZone: String
    var location: GeocodeLocation
    var country: String?
    var subThoroughfare: String?
    var thoroughfare: String?
    var name: String
    var locality: String?
    var areasOfInterest: Array<String>?
    var ocean: String?
    var subLocality: String?
    var postalCode: String?
    var administrativeArea: String?
    var inlandWater: String?
}