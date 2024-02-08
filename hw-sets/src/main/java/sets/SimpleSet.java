package sets;


import java.util.List;

/**
 * Represents an immutable set of points on the real line that is easy to
 * describe, either because it is a finite set, e.g., {p1, p2, ..., pN}, or
 * because it excludes only a finite set, e.g., R \ {p1, p2, ..., pN}. As with
 * FiniteSet, each point is represented by a Java float with a non-infinite,
 * non-NaN value.
 */
public class SimpleSet {

  // TODO: fill in and document the representation
  //       Make sure to include the representation invariant (RI)
  //       and the abstraction function (AF).
  // values are stored in an array, in sorted order, the array could either be a finite set if boolean complement
  // is set to false; or it could exclude a finite set if set to true.
  //
  // RI: vals[0] < vals[1] < ... < vals[vals.length-1], either finite or not
  // AF(this) = {vals[0], vals[1], ..., vals[vals.length-1]}
  // if not complement then this is finite otherwise not finite
  private FiniteSet values;
  private Boolean complement;



  /**
   * Creates a simple set containing only the given points.
   * @param vals Array containing the points to make into a SimpleSet
   * @spec.requires points != null and has no NaNs, no infinities, and no dups
   * @spec.effects this = {vals[0], vals[1], ..., vals[vals.length-1]}
   */
  public SimpleSet(float[] vals) {
    // TODO: implement this
    this.values = FiniteSet.of(vals);
    this.complement = false;
  }

  /**
   * Private constructor that directly fills in the fields above.
   * @param complement Whether this = points or this = R \ points
   * @param points List of points that are in the set or not in the set
   * @spec.requires points != null
   * @spec.effects this = R \ points if complement else points
   */
  private SimpleSet(boolean complement, FiniteSet points) {
    // TODO: implement this
    this.values = points;
    this.complement = complement;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SimpleSet))
      return false;

    SimpleSet other = (SimpleSet) o;

    // TODO: replace this with a correct check
    if(this.complement == false && other.complement == false){
      return this.values.equals(other.values);
    } else if (this.complement == true && other.complement == true){
      return this.values.equals(other.values);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * Returns the number of points in this set.
   * @return N      if this = {p1, p2, ..., pN} and
   *         infty  if this = R \ {p1, p2, ..., pN}
   */
  public float size() {
    // TODO: implement this
    // We check if the set is finite, if it is then we just return the size of the set.
    // otherwise we return infty.
    if (!complement){
      return values.size();
    } else {
      return Float.POSITIVE_INFINITY;
    }
  }

  /**
   * Returns a string describing the points included in this set.
   * @return the string "R" if this contains every point,
   *     a string of the form "R \ {p1, p2, .., pN}" if this contains all
   *        but {@literal N > 0} points, or
   *     a string of the form "{p1, p2, .., pN}" if this contains
   *        {@literal N >= 0} points,
   *     where p1, p2, ... pN are replaced by the individual numbers.
   */
  public String toString() {
    // TODO: implement this with a loop. document its invariant
    //       a StringBuilder may be useful for creating the string

    StringBuilder output = new StringBuilder();
    // if values is a non-finite set then we will append R to the
    if(complement){
      output.append("R");
    }
    // if values in a non-finite set and is not null then we will append \
    if(values.size() > 0 && complement){
      output.append(" / ");
    }
    // if the size of value is zero and a finite set then we append []
    if(values.size() == 0 && !complement){
      output.append("[]");
    }
    // whether the set is finite or not it has to be greater than 0, so we could append
    // all the values in the set into the output.
    if(values.size() > 0) {
      List<Float> x = values.getPoints();
      output.append("[" + x.get(0));
      //{inv: buf = values[0] + values[1] ... + values[i - 1];
      for (int i = 1; i < values.size(); i++) {
        output.append(", " + x.get(i));
      }
      output.append("]");
    }
    return output.toString();
  }

  /**
   * Returns a set representing the points R \ this.
   * @return R \ this
   */
  public SimpleSet complement() {
    // TODO: implement this method
    //       include sufficient comments to see why it is correct (hint: cases)

    // we check if set is finite, if the set is not finite then we return a simple set of a finite set with the same
    // values
    // if the set was finite we return is simple set with the same set, set to not finite.
    if(complement){
      return new SimpleSet(false, this.values);
    } else {
      return new SimpleSet(true, this.values);
    }
  }

  /**
   * Returns the union of this and other.
   * @param other Set to union with this one.
   * @spec.requires other != null
   * @return this union other
   */
  public SimpleSet union(SimpleSet other) {
    // TODO: implement this method
    //       include sufficient comments to see why it is correct (hint: cases)

    // if both sets are finite then we could just union them together
    if(this.complement == false && other.complement == false){
      return new SimpleSet(false, this.values.union(other.values));
    }
    // if both sets are not finite then we should return their intersection since that means those values are not in
    // the set.
    else if (this.complement == true && other.complement == true){
      return new SimpleSet(true, this.values.intersection(other.values));
    }
    // if this is not finite and other is finite then finding the difference between this and other
    // returns a non-finite set that has values only found in this and not other.
    else if(this.complement == true && other.complement == false){
      return new SimpleSet(true, this.values.difference(other.values));
    }
    // if this is finite and other is not finite then finding the difference between other and this
    // returns a non-finite set that has values only found in other and not this.
    else{
      return new SimpleSet(true, other.values.difference(this.values));
    }
  }

  /**
   * Returns the intersection of this and other.
   * @param other Set to intersect with this one.
   * @spec.requires other != null
   * @return this intersect other
   */
  public SimpleSet intersection(SimpleSet other) {
    // TODO: implement this method
    //       include sufficient comments to see why it is correct
    // NOTE: There is more than one correct way to implement this.

    // if both sets are finite then we could just find the intersection of both sets.
    if(this.complement == false && other.complement == false){
      return new SimpleSet(false, this.values.intersection(other.values));
    }
    // if both sets are not finite then using the union get us a non-finite set that has the values found in
    // both sets.
    else if (this.complement == true && other.complement == true){
      return new SimpleSet(true, this.values.union(other.values));
    }
    // if this does not have a finite set and other does then finding the difference between other
    // and this gives us a set that is finite and has only the values found in other and not in this.
    else if(this.complement == true && other.complement == false){
      return new SimpleSet(false, other.values.difference(this.values));
    }
    // if this does have a finite set and other does not then finding the difference between this
    // and other gives us a set that is finite and has only the values found in this and are not other.
    else{
      return new SimpleSet(false, this.values.difference(other.values));
    }
  }

  /**
   * Returns the difference of this and other.
   * @param other Set to difference from this one.
   * @spec.requires other != null
   * @return this minus other
   */
  public SimpleSet difference(SimpleSet other) {
    // TODO: implement this method
    //       include sufficient comments to see why it is correct
    // NOTE: There is more than one correct way to implement this.

    // if both sets are finite then we could just find the difference between both sets.
    if(this.complement == false && other.complement == false){
      return new SimpleSet(false, this.values.difference(other.values));
    }
    // if both sets are not finite then we should the difference of this other and this to get a finite set
    // that only has the values found in other and not in this.
    else if (this.complement == true && other.complement == true){
      return new SimpleSet(false, other.values.difference(this.values));
    }
    // if this is not a finite set and other is then unioning both sets gets us a non-finite set that has
    // the values found in both sets.
    else if(this.complement == true && other.complement == false){
      return new SimpleSet(true, other.values.union(this.values));
    }
    // if this is finite and other is not a finite set then intersecting both sets gives us a finite set that
    // has only the values shared by both sets.
    else{
      return new SimpleSet(false, this.values.intersection(other.values));
    }
  }

}
